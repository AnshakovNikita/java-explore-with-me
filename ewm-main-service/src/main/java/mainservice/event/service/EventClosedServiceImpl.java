package mainservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mainservice.category.repository.CategoryRepository;
import mainservice.event.dto.EventFullDto;
import mainservice.event.dto.EventShortDto;
import mainservice.event.dto.NewEventDto;
import mainservice.event.dto.UpdateEventRequest;
import mainservice.event.mapper.EventMapper;
import mainservice.event.model.Event;
import mainservice.event.model.EventStatus;
import mainservice.event.repository.EventRepository;
import mainservice.exceptions.ConflictException;
import mainservice.exceptions.NotFoundException;
import mainservice.exceptions.ValidationException;
import mainservice.user.repository.UserRepository;
import mainservice.utils.FromSizeRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static mainservice.event.mapper.EventMapper.toEventShortDto;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class EventClosedServiceImpl implements  EventClosedService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");



    @Override
    public List<EventShortDto> getEventsOfUser(Long userId, int from, int size) {
        userValidation(userId);
        Pageable pageable = FromSizeRequest.of(from, size);

        List<EventShortDto> eventShortDtos = eventRepository.findByInitiatorId(userId, pageable).stream()
                .map(event -> toEventShortDto(event))
                .collect(toList());
        log.info("get Events Of User" + userId);
        return eventShortDtos;
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, UpdateEventRequest updateEventRequest) {
        userValidation(userId);
        Event event = eventRepository.findById(updateEventRequest.getEventId())
                .orElseThrow(() -> new NotFoundException("Event with id=" + updateEventRequest.getEventId() +
                        " not found."));
        Long eventIniciatorId = event.getInitiator().getId();
        if (!userId.equals(eventIniciatorId)) {
            throw new ConflictException("This user is not the initiator");
        }
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
        if (event.getState().equals(EventStatus.CANCELED)) {
            event.setState(EventStatus.PENDING);
        }
        EventFullDto dto = EventMapper.toEventFullDto(eventRepository.save(event));
        log.info("update event: " + dto.getId());
        return dto;
    }

    @Override
    @Transactional
    public EventFullDto postEvent(Long userId, NewEventDto dto) {
        userValidation(userId);

        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new ValidationException("?????????????? ???????????????????? ??????????????????????");
        }
        if (dto.getAnnotation() == null || dto.getAnnotation().isBlank()) {
            throw new ValidationException("?????????????? ???????????????????? ??????????????????????");
        }

        LocalDateTime now = LocalDateTime.now();
        if (LocalDateTime.parse(dto.getEventDate(), formatter).isBefore(now.plusHours(2))) {
            throw new ValidationException("?????????????? ???????????? ???????????????? ???? ??????????????, ?????? ?????????? ?????? ???????? ???? ???????????????? ??????????????");
        }

        Event event = Event.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .annotation(dto.getAnnotation())
                .category(categoryRepository.findById(dto.getCategory()).get())
                .paid(dto.getPaid())
                .eventDate(LocalDateTime.parse(dto.getEventDate(), formatter))
                .initiator(userRepository.findById(userId).get())
                .description(dto.getDescription())
                .participantLimit(dto.getParticipantLimit())
                .confirmedRequests(0)
                .state(EventStatus.PENDING)
                .createdOn(now)
                .lat(dto.getLocation().getLat())
                .lon(dto.getLocation().getLon())
                .requestModeration(dto.getRequestModeration())
                .build();

        eventRepository.save(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        log.info("post event" + eventFullDto.getId());
        return eventFullDto;
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        eventValidation(eventId);
        userValidation(userId);
        Event event = eventRepository.findById(eventId).get();
        Long iniciatorId = event.getInitiator().getId();
        initiatorValidation(userId, iniciatorId);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.save(event));
        log.info("get Event: " + eventId);
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        eventValidation(eventId);
        userValidation(userId);
        Event event = eventRepository.findById(eventId).get();
        initiatorValidation(userId, event.getInitiator().getId());
        if (!event.getState().equals(EventStatus.PENDING)) {
            throw new ValidationException("Only PENDING state events can be cancelled");
        }
        event.setState(EventStatus.CANCELED);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.save(event));
        log.info("cancel event" + eventId);
        return eventFullDto;
    }

    private void userValidation(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User with id = " + id + " not found");
        }
    }

    private void eventValidation(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new NotFoundException("Event with id = " + id + " not found");
        }
    }

    private void initiatorValidation(Long userId, Long initiatorId) {
        if (!userId.equals(initiatorId)) {
            throw new ValidationException("Only the creator of the event can perform this action on it.");
        }
    }
}
