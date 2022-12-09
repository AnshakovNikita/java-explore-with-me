package stats.service;

import org.springframework.stereotype.Service;
import stats.dto.ShortStatDto;
import stats.dto.EndpointHit;
import stats.repository.StatRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static stats.mapper.StatMapper.toStat;
import static stats.mapper.StatMapper.toStatDto;

@Service
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    public StatServiceImpl(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EndpointHit addStat(EndpointHit endpointHit) {
        return toStatDto(statRepository.save(toStat(endpointHit)));
    }

    public List<ShortStatDto> getStat(List<String> uris, String start, String end, Boolean unique) {

        LocalDateTime startFormatted = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime endFormatted = LocalDateTime.parse(end, FORMATTER);

        List<Object[]> endpointHits;
        if (unique.equals(true)) {
            endpointHits = statRepository.getEndpointHitsUnique(startFormatted, endFormatted, uris);
        } else {
            endpointHits = statRepository.getEndpointHitsNotUnique(startFormatted, endFormatted, uris);
        }
        List<ShortStatDto> shortStatDtos = new ArrayList<>();

        if (!endpointHits.isEmpty()) {
            for (Object[] object : endpointHits) {
                ShortStatDto shortStatDto = new ShortStatDto();
                shortStatDto.setApp(object[0].toString());
                shortStatDto.setUri(object[1].toString());
                shortStatDto.setHits(Long.valueOf(object[2].toString()));
                shortStatDtos.add(shortStatDto);
            }
        }
        return shortStatDtos;
    }
}