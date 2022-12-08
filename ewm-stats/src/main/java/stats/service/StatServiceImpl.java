package stats.service;

import org.springframework.stereotype.Service;
import stats.dto.ShortStatDto;
import stats.dto.EndpointHit;
import stats.mapper.StatMapper;
import stats.model.Stat;
import stats.repository.StatRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static stats.mapper.StatMapper.toStat;
import static stats.mapper.StatMapper.toStatDto;

@Service
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    public StatServiceImpl(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EndpointHit addStat(EndpointHit endpointHit) {
        String s = endpointHit.getUri();
        s = s.replace("[", "");
        s = s.replace("]", "");
        endpointHit.setHits(0L);
        endpointHit.setUri(s);
        return toStatDto(statRepository.save(toStat(endpointHit)));
    }

    @Override
    public List<ShortStatDto> getStat(List<String> uris, String start, String end, Boolean unique) {

        List<Stat> stats;
        if (!unique) {
            stats = statRepository.findAllByUris(uris, LocalDateTime.parse(start, formatter),
                    LocalDateTime.parse(end, formatter));
        } else {
            stats = statRepository.findAllByUniqUris(uris, LocalDateTime.parse(start, formatter),
                    LocalDateTime.parse(end, formatter));
        }
        List<ShortStatDto> shortStatDtos = stats.stream()
                .map(StatMapper::toShortStatDto)
                .collect(Collectors.toList());
        shortStatDtos
                .forEach(shortStatDto -> shortStatDto.setHits(statRepository.countByUri(shortStatDto.getUri())));
        return shortStatDtos;
    }
}