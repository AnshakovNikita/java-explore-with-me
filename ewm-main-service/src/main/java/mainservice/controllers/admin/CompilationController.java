package mainservice.controllers.admin;

import lombok.extern.slf4j.Slf4j;
import mainservice.compilation.dto.CompilationDto;
import mainservice.compilation.dto.NewCompilationDto;
import mainservice.compilation.service.CompilationServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/admin")
@Slf4j
public class CompilationController {

    private final CompilationServiceImpl compilationService;

    public CompilationController(CompilationServiceImpl compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping("/compilations")
    public CompilationDto saveCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("save compilation={}", newCompilationDto);
        return compilationService.saveCompilation(newCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("delete event={} from compilation={}", eventId, compId);
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public NewCompilationDto addEventToCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("add event={} to compilation={}", eventId, compId);
        return compilationService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/compilations/{compId}/pin")
    public void unPinCompilation(@PathVariable Long compId) {
        log.info("unpin compilation={}", compId);
        compilationService.unPinCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        log.info("pin compilation={}", compId);
        compilationService.pinCompilation(compId);
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("delete compilation={}", compId);
        compilationService.deleteCompilation(compId);
    }
}
