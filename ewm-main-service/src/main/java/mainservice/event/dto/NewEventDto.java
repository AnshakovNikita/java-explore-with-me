package mainservice.event.dto;

import lombok.*;
import mainservice.event.model.EventStatus;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class NewEventDto {
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String annotation;
    private Long category;
    @NotBlank
    private String description;
    @NotBlank
    private String eventDate;
    private Long initiator;
    private Location location;
    private Boolean paid;
    private EventStatus state;
    private LocalDateTime createdOn;
    private Integer participantLimit;
    private Boolean requestModeration;
}
