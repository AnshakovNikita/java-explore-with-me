package stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class EndpointHit {
    private Long id;
    @NotBlank
    @NotNull
    private String app;
    @NotBlank
    @NotNull
    private String uri;
    @NotBlank
    @NotNull
    private String ip;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private Long hits;
}
