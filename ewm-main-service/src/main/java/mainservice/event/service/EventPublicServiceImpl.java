package mainservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mainservice.client.StatWebClient;
import mainservice.event.dto.EventFullDto;
import mainservice.event.dto.EventShortDto;
import mainservice.event.mapper.EventMapper;
import mainservice.event.model.Event;
import mainservice.event.model.EventStatus;
import mainservice.event.repository.EventRepository;
import mainservice.exceptions.ConflictException;
import mainservice.exceptions.NotFoundException;
import mainservice.exceptions.ValidationException;
import mainservice.stats.EndpointHit;
import mainservice.stats.ViewStats;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventPublicServiceImpl implements EventPublicService {
    private final EventRepository repository;
    private final StatWebClient client;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final LocalDateTime START_FOR_COLLECTING_STAT = LocalDateTime.now().minusYears(10);
    private static final LocalDateTime RANGE_END_FOR_COLLECTING_EVENTS = LocalDateTime.now().plusYears(300);


    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                         Boolean onlyAvailable, String sort,
                                         Integer from, Integer size, HttpServletRequest request) {
        rangeStart = (rangeStart != null) ? rangeStart : LocalDateTime.now();
        rangeEnd = (rangeEnd != null) ? rangeEnd : RANGE_END_FOR_COLLECTING_EVENTS;
        createStat(request);
        if (rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("The end date and time of the event cannot " +
                    "be earlier than the start date of the event.");
        }

        List<Event> events;

        if (categories != null) {
            events = repository.findByCategoryIdsAndText(text, categories);
        } else {
            events = repository.findByText(text);
        }

        List<EventShortDto> eventShortDtos = events.stream()
                .map(dtos -> EventMapper.toEventShortDto(dtos))
                .collect(toList());
        eventShortDtos = eventShortDtos.stream()
                .sorted(Comparator.comparing(EventShortDto::getEventDate).reversed())
                .skip(from)
                .limit(size)
                .collect(toList());
        log.info("get events");
        return eventShortDtos;
    }

    @Override
    public EventFullDto getEventById(Long id, HttpServletRequest request) {
        eventValidation(id);
        createStat(request);
        EventFullDto dto = EventMapper.toEventFullDtoViews(repository.findById(id).get(),
                                                           getViews(request, false));
        if (!EventStatus.PUBLISHED.equals(dto.getState())) {
            throw new ConflictException("Event not published.");
        }
        log.info("events by id = " + id + " for an unregistered user");
        return dto;

    }

    private void eventValidation(Long id) {
        if (repository.findById(id).isEmpty()) {
            throw new NotFoundException("Events with id = " + id + " не найдено");
        }
    }

    private void createStat(HttpServletRequest request) {
        client.save(EndpointHit.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app("ewm-main-service")
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build());
    }

    private long getViews(HttpServletRequest request, boolean unique) {
        List<ViewStats> statistics = client.getStats(START_FOR_COLLECTING_STAT, LocalDateTime.now(),
                                                     Set.of(request.getRequestURI()), unique);
        long views = 0;
        for (ViewStats view : statistics)
            if (request.getRequestURI().equals(view.getUri()) && "ewm-main-service".equals(view.getApp()))
                views = view.getHits();
        return views;
    }
}
