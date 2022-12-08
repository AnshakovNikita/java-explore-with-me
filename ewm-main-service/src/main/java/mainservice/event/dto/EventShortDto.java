package mainservice.event.dto;

import mainservice.category.dto.CategoryDto;
import mainservice.user.dto.UserShortDto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class EventShortDto {
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String annotation;

    private CategoryDto category;

    private Integer confirmedRequests;

    @NotBlank
    private String eventDate;

    private UserShortDto initiator;

    private Boolean paid;

    private Long views;
}
