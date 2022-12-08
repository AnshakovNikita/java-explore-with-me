package mainservice.event.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateEventRequest {
    @NotBlank
    private Long eventId;
    @NotBlank
    private String annotation;
    @NotBlank
    private Long categoryId;
    private String description;
    @NotBlank
    private Location location;
    @NotBlank
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String eventDate;
    @NotBlank
    private String title;
}
