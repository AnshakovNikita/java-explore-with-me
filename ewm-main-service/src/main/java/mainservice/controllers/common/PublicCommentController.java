package mainservice.controllers.common;

import lombok.extern.slf4j.Slf4j;
import mainservice.comment.dto.CommentDto;
import mainservice.comment.mapper.CommentMapper;
import mainservice.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/events")
public class PublicCommentController {

    private final CommentService commentService;

    @Autowired
    public PublicCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{eventId}/comments/{commentId}")
    public CommentDto getCommentByEventById(@PathVariable Long eventId,
                                            @PathVariable Long commentId) {
        log.info("Has received request to endpoint GET/event/{}/comments/{}", eventId, commentId);
        return CommentMapper.toCommentDto(commentService.getCommentById(commentId));
    }

    @GetMapping("/{eventId}/comments")
    public Collection<CommentDto> getAllCommentsByEvent(@PathVariable Long eventId,
                                                        @RequestParam(defaultValue = "0") Integer from,
                                                        @RequestParam(defaultValue = "10") Integer size) {
        log.info("Has received request to endpoint GET/events/{}/comments?from={}size={}",
                eventId, from, size);
        return CommentMapper.toCommentDtoCollection(commentService.getAllCommentsByEvent(eventId, from, size));
    }

}
