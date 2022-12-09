package mainservice.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String email;
}
