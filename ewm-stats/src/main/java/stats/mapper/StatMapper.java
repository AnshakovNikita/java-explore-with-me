package stats.mapper;

import org.springframework.stereotype.Component;
import stats.dto.ShortStatDto;
import stats.dto.EndpointHit;
import stats.model.Stat;

@Component
public class StatMapper {


    public static Stat toStat(EndpointHit endpointHit) {
        return Stat.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
                .build();
    }

    public static EndpointHit toStatDto(Stat stat) {
        return EndpointHit.builder()
                .id(stat.getId())
                .app(stat.getApp())
                .uri(stat.getUri())
                .ip(stat.getIp())
                .timestamp(stat.getTimestamp())
                .build();
    }

    public static ShortStatDto toShortStatDto(Stat stat) {
        return ShortStatDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .build();
    }
}