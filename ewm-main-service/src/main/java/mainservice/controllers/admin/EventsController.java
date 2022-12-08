package mainservice.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mainservice.event.dto.EventFullDto;
import mainservice.event.dto.UpdateEventRequest;
import mainservice.event.model.EventStatus;
import mainservice.event.service.EventAdminService;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
public class EventsController {
    private final EventAdminService service;


    @GetMapping("/events")
    public List<EventFullDto> getEvents(@RequestParam(name = "users", required = false) List<Long> users,
                                        @RequestParam(name = "states", required = false) List<EventStatus> statusList,
                                        @RequestParam(name = "categories", required = false) List<Long> categories,
                                        @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                        @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                        @RequestParam(name = "size", defaultValue = "10") @Positive int size) {

        List<EventFullDto> dtos = service.getEvents(users, statusList, categories, rangeStart, rangeEnd, from, size);
        log.info("get Events");
        return dtos;
    }

    @PutMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId, @RequestBody UpdateEventRequest updateEventRequest) {
        EventFullDto dto = service.updateEvent(eventId, updateEventRequest);
        log.info("update Event: " + eventId);
        return dto;
    }

    @PatchMapping("/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Long eventId) {
        EventFullDto dto = service.publishEvent(eventId);
        log.info("publish Event: " + eventId);
        return dto;
    }

    @PatchMapping("/events/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable Long eventId) {
        EventFullDto dto = service.rejectEvent(eventId);
        log.info("reject Event: " + eventId);
        return dto;
    }
}
