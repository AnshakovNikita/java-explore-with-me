package mainservice.event.mapper;

import mainservice.category.mapper.CategoryMapper;
import mainservice.event.dto.EventFullDto;
import mainservice.event.dto.EventShortDto;
import mainservice.event.dto.Location;
import mainservice.event.model.Event;
import mainservice.user.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EventMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().format(formatter))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public static Event toEvent(EventFullDto eventFullDto) {
        return Event.builder()
                .id(eventFullDto.getId())
                .annotation(eventFullDto.getAnnotation())
                .title(eventFullDto.getTitle())
                .category(eventFullDto.getCategory())
                .paid(eventFullDto.getPaid())
                .eventDate(LocalDateTime.parse(eventFullDto.getEventDate(), formatter))
                .initiator(UserMapper.toUserFromShort(eventFullDto.getInitiator()))
                .description(eventFullDto.getDescription())
                .participantLimit(eventFullDto.getParticipantLimit())
                .confirmedRequests(eventFullDto.getConfirmedRequests())
                .lat(eventFullDto.getLocation().getLat())
                .lon(eventFullDto.getLocation().getLon())
                .state(eventFullDto.getState())
                .createdOn(eventFullDto.getCreatedOn())
                .publishedOn(eventFullDto.getPublishedOn())
                .requestModeration(eventFullDto.getRequestModeration())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        Location location = new Location();
        location.setLat(event.getLat());
        location.setLon(event.getLon());

        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(formatter))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(location)
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .confirmedRequests(event.getConfirmedRequests())
                .state(event.getState())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .build();
    }

    public static EventFullDto toEventFullDtoViews(Event event, Long views) {
        Location location = new Location();
        location.setLat(event.getLat());
        location.setLon(event.getLon());

        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(formatter))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(location)
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .confirmedRequests(event.getConfirmedRequests())
                .state(event.getState())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .views(views)
                .build();
    }
}
