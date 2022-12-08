package mainservice.controllers.closed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mainservice.event.dto.EventFullDto;
import mainservice.event.dto.EventShortDto;
import mainservice.event.dto.NewEventDto;
import mainservice.event.dto.UpdateEventRequest;
import mainservice.event.service.EventClosedService;
import mainservice.exceptions.ValidationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Validated
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventsController {
    private final EventClosedService eventService;

    @GetMapping
    List<EventShortDto> userEvents(@PathVariable Long userId,
                                   @RequestParam(defaultValue = "0") int from,
                                   @RequestParam(defaultValue = "10") int size) {
        List<EventShortDto> dtos = eventService.getEventsOfUser(userId, from, size);
        log.info("user Events: " + userId);
        return dtos;
    }

    @PatchMapping
    EventFullDto updateEvent(@PathVariable Long userId,
                             @RequestBody UpdateEventRequest event) {
        EventFullDto dto = eventService.updateEvent(userId, event);
        log.info("update Event: " + userId);
        return dto;
    }


    @PostMapping
    EventFullDto createEvent(@PathVariable Long userId,
                             @RequestBody NewEventDto event) {
        if (event.getTitle() == null || event.getTitle().isBlank()) {
            throw new ValidationException("Событие составлено некорректно");
        }
        if (event.getAnnotation() == null || event.getAnnotation().isBlank()) {
            throw new ValidationException("Событие составлено некорректно");
        }
        EventFullDto dto = eventService.postEvent(userId, event);
        log.info("create Event: " + userId);
        return dto;
    }

    @GetMapping(value = "{eventId}")
    EventFullDto getEventByUserIdAndEventId(@PathVariable Long userId,
                                            @PathVariable Long eventId) {
        EventFullDto dto = eventService.getEvent(userId, eventId);
        log.info("get Event By UserId: " + userId + "And Event Id: " + eventId);
        return dto;
    }

    @PatchMapping(value = "{eventId}")
    EventFullDto cancelEventByUserIdAndEventId(@PathVariable Long userId,
                                               @PathVariable Long eventId) {
        EventFullDto dto = eventService.cancelEvent(userId, eventId);
        log.info("cancel Event By UserId: " + userId + "And Event Id: " + eventId);
        return dto;
    }
}
