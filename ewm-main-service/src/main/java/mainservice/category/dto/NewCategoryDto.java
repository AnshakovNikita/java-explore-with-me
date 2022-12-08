package mainservice.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class NewCategoryDto {
    @NotBlank
    private String name;
}
