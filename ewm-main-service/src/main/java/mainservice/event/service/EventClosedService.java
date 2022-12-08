package mainservice.event.service;

import mainservice.event.dto.EventFullDto;
import mainservice.event.dto.EventShortDto;
import mainservice.event.dto.NewEventDto;
import mainservice.event.dto.UpdateEventRequest;
import mainservice.request.dto.RequestDto;

import java.util.List;

public interface EventClosedService {
    List<EventShortDto> getEventsOfUser(Long userId, int from, int size);

    EventFullDto updateEvent(Long userId, UpdateEventRequest event);

    EventFullDto postEvent(Long userId, NewEventDto dto);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto cancelEvent(Long userId, Long eventId);

    List<RequestDto> getRequests(Long userId, Long eventId);

    RequestDto confirmRequest(Long userId, Long eventId, Long reqId);

    RequestDto rejectRequest(Long userId, Long eventId, Long reqId);
}
