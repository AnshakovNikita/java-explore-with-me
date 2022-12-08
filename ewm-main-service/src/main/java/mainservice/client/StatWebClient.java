package mainservice.client;

import lombok.RequiredArgsConstructor;
import mainservice.stats.EndpointHit;
import mainservice.stats.ViewStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StatWebClient {

    private final WebClient client;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatWebClient(@Value("${explore-server.url}") String serverUrl) {
        final DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("http://localhost:9090");
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        client = WebClient
                .builder()
                .uriBuilderFactory(factory)
                .defaultHeader("Content-Type", "application/json")
                .baseUrl("http://localhost:9090")
                .build();
    }


    public void save(EndpointHit endpointHit) {
        client.post()
                .uri("/hit")
                .body(Mono.just(endpointHit), EndpointHit.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(EndpointHit.class)
                .block();
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, Set<String> uris, boolean unique) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", encodeLocalDateTimeValue(start))
                        .queryParam("end", encodeLocalDateTimeValue(end))
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(new ParameterizedTypeReference<List<ViewStats>>() {
                })
                .block();
    }

    private String encodeLocalDateTimeValue(LocalDateTime time) {
        return URLEncoder.encode(time.format(formatter), StandardCharsets.UTF_8);
    }

}