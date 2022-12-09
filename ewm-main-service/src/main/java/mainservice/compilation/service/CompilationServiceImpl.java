package mainservice.compilation.service;

import mainservice.compilation.dto.CompilationDto;
import mainservice.compilation.dto.NewCompilationDto;
import mainservice.compilation.model.Compilation;
import mainservice.compilation.repository.CompilationRepository;
import mainservice.event.model.Event;
import mainservice.event.repository.EventRepository;
import mainservice.exceptions.NotFoundException;
import mainservice.exceptions.ValidationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static mainservice.compilation.mapper.CompilationMapper.*;

@Service
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getTitle() == null || newCompilationDto.getTitle().isBlank()) {
            throw new ValidationException("Заголовок подборки не может быть пустым.");
        }

        Set<Event> events = new HashSet<>(eventRepository.findAllById(newCompilationDto.getEvents()));
        Compilation compilation = compilationRepository.save(toCompilationFromNew(newCompilationDto, events));
        compilation.setEvents(events);
        return toCompilationDto(compilation);
    }

    @Override
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборки не существует."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new NotFoundException("События не существует."));
        Set<Event> events = compilation.getEvents();
        events.remove(event);
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборки не существует."));
        return toCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> getRequiredCompilations(Boolean pinned, Integer from, Integer size) {
        int page = from / size;
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        List<Compilation> compilations = compilationRepository.findAllByPinnedIsTrue(pinned, pageRequest);
        List<CompilationDto> compilationDtos = new ArrayList<>();
        CompilationDto compilationDto;
        for (Compilation compilation : compilations) {
            compilationDto = toCompilationDto(compilation);
            compilationDtos.add(compilationDto);
        }
        return compilationDtos;
    }

    @Override
    public NewCompilationDto addEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборки не существует."));
        Set<Event> events = compilation.getEvents();
        events.add(eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new NotFoundException("События не существует.")));
        compilation.setEvents(events);
        compilationRepository.save(compilation);
        return toNewCompilationDto(compilation);
    }

    @Override
    public void unPinCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборки не существует."));
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public void pinCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборки не существует."));
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }
}
