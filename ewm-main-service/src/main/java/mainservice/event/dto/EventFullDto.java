package mainservice.event.dto;

import lombok.*;

import mainservice.category.model.Category;
import mainservice.event.model.EventStatus;
import mainservice.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class EventFullDto {
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String annotation;

    private Category category;

    private Boolean paid;

    @NotBlank
    private String eventDate;

    private UserShortDto initiator;

    @NotBlank
    private String description;

    private Integer participantLimit;

    private Integer confirmedRequests;

    private EventStatus state;

    private LocalDateTime publishedOn;

    private LocalDateTime createdOn;

    private Location location;

    private Boolean requestModeration;

    private Long views;
}
