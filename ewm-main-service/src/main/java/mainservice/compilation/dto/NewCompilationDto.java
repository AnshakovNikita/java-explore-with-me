package mainservice.compilation.dto;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "title")
@Builder
public class NewCompilationDto {
    private Long id;
    private Boolean pinned;
    private String title;
    private Set<Long> events;
}
