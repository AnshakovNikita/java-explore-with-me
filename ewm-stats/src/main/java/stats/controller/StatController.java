package stats.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stats.dto.ShortStatDto;
import stats.dto.EndpointHit;
import stats.service.StatServiceImpl;

import java.util.List;

@RestController
@Slf4j
public class StatController {
    private final StatServiceImpl service;

    @Autowired
    public StatController(StatServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/hit")
    public EndpointHit addStat(@RequestBody EndpointHit endpointHit) {
        log.info("Creating hit={}", endpointHit);
        return service.addStat(endpointHit);
    }

    @GetMapping("/stats")
    public List<ShortStatDto> getStat(@RequestParam(name = "uris", required = false) List<String> uris,
                                      @RequestParam(name = "start",
                                              defaultValue = "1950-01-01 00:00:00") String start,
                                      @RequestParam(name = "end",
                                              defaultValue = "2090-01-01 00:00:00") String end,
                                      @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        log.info("Get hits uris={}, start={}, end={}, unique={}", uris, start, end, unique);
        return service.getStat(uris, start, end, unique);
    }
}
