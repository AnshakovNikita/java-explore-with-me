package mainservice.event.service;

import mainservice.event.dto.EventFullDto;
import mainservice.event.dto.EventShortDto;
import mainservice.event.dto.NewEventDto;
import mainservice.event.dto.UpdateEventRequest;

import java.util.List;

public interface EventClosedService {
    List<EventShortDto> getEventsOfUser(Long userId, int from, int size);

    EventFullDto updateEvent(Long userId, UpdateEventRequest event);

    EventFullDto postEvent(Long userId, NewEventDto dto);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto cancelEvent(Long userId, Long eventId);

}
