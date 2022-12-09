package mainservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mainservice.category.repository.CategoryRepository;
import mainservice.event.dto.EventFullDto;
import mainservice.event.dto.UpdateEventRequest;
import mainservice.event.model.Event;
import mainservice.event.model.EventStatus;
import mainservice.event.repository.EventRepository;
import mainservice.exceptions.NotFoundException;
import mainservice.exceptions.ValidationException;
import mainservice.utils.FromSizeRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static mainservice.event.mapper.EventMapper.toEventFullDto;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class EventAdminServiceImpl implements EventAdminService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final List<EventStatus> STATUS_LIST = Arrays.asList(EventStatus.PENDING,
                                                                       EventStatus.CANCELED,
                                                                       EventStatus.PUBLISHED);

    @Override
    public List<EventFullDto> getEvents(List<Long> users, List<EventStatus> statusList,
                                        List<Long> categories, String rangeStart,
                                        String rangeEnd, int from, int size) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null) {
            try {
                start = LocalDateTime.parse(rangeStart, formatter);
            } catch (ValidationException e) {
                throw new ValidationException("Incorrect rangeStart value = " + rangeStart);
            }
        }
        if (rangeEnd != null) {
            try {
                end = LocalDateTime.parse(rangeEnd, formatter);
            } catch (ValidationException e) {
                throw new ValidationException("Incorrect rangeEnd value = " + rangeEnd);
            }
        }

        start = (rangeStart != null) ? start : LocalDateTime.now();
        end = (rangeEnd != null) ? end : LocalDateTime.now().plusYears(300);

        if (start.isAfter(end)) {
            throw new ValidationException("Ending event before it starts");
        }

        if (statusList == null) {
            statusList = STATUS_LIST;
        }

        List<Event> events = new ArrayList<>();
        Pageable pageable = FromSizeRequest.of(from, size);

        if ((categories != null) && (users != null)) {
            events = eventRepository.findByUsersAndCategoriesAndStates(users, categories, statusList, pageable);
        }

        if ((categories == null) && (users == null)) {
            events = eventRepository.findByStates(statusList, pageable);
        }

        if ((categories != null) && (users == null)) {
            events = eventRepository.findByCategoriesAndStates(categories, statusList, pageable);
        }

        if ((categories == null) && (users != null)) {
            events = eventRepository.findByUsersAndStates(users, statusList, pageable);
        }

        List<EventFullDto> eventFullDtos = events.stream()
                .map(event -> toEventFullDto(event))
                .collect(toList());
        log.info("get events");
        return eventFullDtos;
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long eventId, UpdateEventRequest updateEventRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " not found."));

        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategoryId() != null) {
            event.setCategory(categoryRepository.findById(updateEventRequest.getCategoryId()).get());
        }

        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(updateEventRequest.getEventDate(), formatter));
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventRequest.getRequestModeration());
        }
        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }

        event = eventRepository.save(event);
        log.info("update Event: " + eventId);
        return toEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto publishEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id = " + eventId + " not found."));
        if (!event.getState().equals(EventStatus.PENDING)) {
            throw new ValidationException("State must be PENDING. Now - " + event.getState());
        }
        if (event.getEventDate().equals(LocalDateTime.now().plusHours(1))) {
            throw new ValidationException("Too late to update this event.");
        }
        event.setState(EventStatus.PUBLISHED);
        EventFullDto eventDto = toEventFullDto(eventRepository.save(event));
        log.info("publish Event: " + eventId);
        return eventDto;
    }

    @Override
    @Transactional
    public EventFullDto rejectEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " not found."));
        if (event.getState().equals(EventStatus.PUBLISHED)) {
            throw new ValidationException("Cannot be rejected");
        }
        event.setState(EventStatus.CANCELED);
        EventFullDto eventDto = toEventFullDto(eventRepository.save(event));
        log.info("reject Event: " + eventId);
        return eventDto;
    }
}
