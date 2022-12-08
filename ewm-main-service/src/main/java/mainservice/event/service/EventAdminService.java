package mainservice.event.service;

import mainservice.event.dto.EventFullDto;
import mainservice.event.dto.UpdateEventRequest;
import mainservice.event.model.EventStatus;

import java.util.List;

public interface EventAdminService {
    List<EventFullDto> getEvents(List<Long> users, List<EventStatus> statusList,
                                 List<Long> categories, String rangeStart,
                                 String rangeEnd, int from, int size);

    EventFullDto updateEvent(Long eventId, UpdateEventRequest updateEventRequest);

    EventFullDto publishEvent(Long eventId);

    EventFullDto rejectEvent(Long eventId);
}
