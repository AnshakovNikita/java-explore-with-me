package mainservice.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCommentDto {
    private Long id;
    @NotBlank
    private String text;
}