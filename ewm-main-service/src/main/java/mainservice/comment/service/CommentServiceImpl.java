package mainservice.comment.service;

import lombok.extern.slf4j.Slf4j;
import mainservice.comment.dto.CommentDto;
import mainservice.comment.dto.NewCommentDto;
import mainservice.comment.dto.UpdateCommentDto;
import mainservice.comment.mapper.CommentMapper;
import mainservice.comment.model.Comment;
import mainservice.comment.repository.CommentRepository;
import mainservice.event.model.Event;
import mainservice.event.model.EventStatus;
import mainservice.event.repository.EventRepository;
import mainservice.exceptions.ConflictException;
import mainservice.exceptions.NotFoundException;
import mainservice.exceptions.ValidationException;
import mainservice.user.model.User;
import mainservice.user.repository.UserRepository;
import mainservice.utils.FromSizeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              UserRepository userRepository,
                              EventRepository eventRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Comment createComment(Long userId, NewCommentDto newCommentDto) {
        if (newCommentDto.getText().isBlank()) {
            throw new ValidationException("Комментарий не может быть пустым.");
        }

        if (newCommentDto.getEvent() == null) {
            throw new NotFoundException("Укажите id события.");
        }
        Comment comment = CommentMapper.toComment(newCommentDto);
        comment.setCreated(LocalDateTime.now());
        Event event = eventRepository.findById(newCommentDto.getEvent())
                .orElseThrow(() -> new NotFoundException("События с id " + newCommentDto.getEvent() + " не существует."));
        comment.setEvent(event);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id " + userId + " не существует."));
        comment.setAuthor(user);

        if (!event.getState().equals(EventStatus.PUBLISHED)) {
            throw new ValidationException("Комментировать можно только опубликованные события.");
        }

        Comment commentDB = commentRepository.save(comment);
        log.info("Комментарий " + commentDB.getId() + " создан.");
        return commentDB;
    }

    @Override
    public Comment updateCommentByAdmin(UpdateCommentDto updateCommentDto) {
        if (updateCommentDto.getText().isBlank()) {
            throw new ValidationException("Комментарий не может быть пустым.");
        }
        Long commentId = updateCommentDto.getId();
        Comment comment = getCommentById(commentId);
        if (updateCommentDto.getText() != null) {
            comment.setText(updateCommentDto.getText());
        }
        Comment commentDB = commentRepository.save(comment);
        log.info("Комментарий " + commentDB.getId() + " обновлен.");
        return commentDB;
    }

    @Override
    public Comment updateCommentByUser(Long userId, UpdateCommentDto updateCommentDto) {
        if (updateCommentDto.getText().isBlank()) {
            throw new ValidationException("Комментарий не может быть пустым.");
        }
        Comment comment = getCommentById(updateCommentDto.getId());
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Комментарий может редактировать только автор.");
        }

        commentRepository.findById(updateCommentDto.getId())
                .orElseThrow(() -> new NotFoundException("Комментарий не существует."));

        if (updateCommentDto.getText() != null) {
            comment.setText(updateCommentDto.getText());
        }
        Comment commentDB = commentRepository.save(comment);
        log.info("Комментарий " + commentDB.getId() + " обновлен.");
        return commentDB;
    }

    @Override
    public Comment getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не существует."));
        log.info("Комментарий " + comment.getId() + " найден.");
        return comment;
    }

    @Override
    public List<Comment> getAllCommentsByEvent(Long eventId, Integer from, Integer size) {
        Pageable pageable = FromSizeRequest.of(from, size);
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("События не существует."));
        return commentRepository.getAllCommentsByEvent(eventId, pageable).stream()
                .collect(Collectors.toList());
    }

    @Override
    public Collection<CommentDto> getAllCommentsByUser(Long userId, Integer from, Integer size) {
        Pageable pageable = FromSizeRequest.of(from, size);

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));

        Collection<Comment> comments = commentRepository.getAllCommentsByUser(userId, pageable).stream()
                .collect(Collectors.toList());

        return CommentMapper.toCommentDtoCollection(comments);
    }

    @Override
    public void deleteCommentByAdmin(Long commId) {
        getCommentById(commId);
        commentRepository.deleteById(commId);
        log.info("Комментарий " + commId + " удален.");
    }

    @Override
    public void deleteCommentByUser(Long userId, Long commId) {
        Comment comment = getCommentById(commId);
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Нельзя удалять чужой комментарий.");
        }
        commentRepository.deleteById(commId);
        log.info("Комментарий " + commId + " удален.");
    }
}