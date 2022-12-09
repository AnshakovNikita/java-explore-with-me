package mainservice.stats;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EndpointHit {
    private Long id;

    private String app;

    private String uri;

    private String ip;

    private String timestamp;
}
