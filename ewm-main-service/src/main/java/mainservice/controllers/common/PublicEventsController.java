package mainservice.controllers.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mainservice.event.dto.EventFullDto;
import mainservice.event.dto.EventShortDto;
import mainservice.event.service.EventPublicService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class PublicEventsController {
    private final EventPublicService eventService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(name = "text", required = false, defaultValue = "") String text,
                                         @RequestParam(name = "categories", required = false) List<Long> categories,
                                         @RequestParam(name = "paid", required = false) Boolean paid,
                                         @RequestParam(name = "rangeStart",
                                                 defaultValue = "1950-01-01 13:30:38") String rangeStart,
                                         @RequestParam(name = "rangeEnd",
                                                 defaultValue = "2090-01-01 00:00:00") String rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(required = false) String sort,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                         @Positive @RequestParam(defaultValue = "10") Integer size,
                                         HttpServletRequest request) {
        List<EventShortDto> dtos = eventService.getEvents(text, categories, paid,
                LocalDateTime.parse(rangeStart, formatter),
                LocalDateTime.parse(rangeEnd, formatter),
                onlyAvailable, sort,
                from, size, request);
        log.info("get Events");
        return dtos;
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable Long eventId, HttpServletRequest request) {
        EventFullDto eventFullDto = eventService.getEventById(eventId, request);
        log.info("get Event By Id: " + eventId);
        return eventFullDto;
    }
}
