package stats.service;

import stats.dto.ShortStatDto;
import stats.dto.EndpointHit;

import java.util.List;

public interface StatService {
    EndpointHit addStat(EndpointHit endpointHit);

    List<ShortStatDto> getStat(List<String> uris, String start, String end, Boolean unique);
}
