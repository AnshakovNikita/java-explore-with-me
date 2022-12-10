package mainservice.controllers.closed;

import lombok.extern.slf4j.Slf4j;
import mainservice.comment.dto.CommentDto;
import mainservice.comment.dto.NewCommentDto;
import mainservice.comment.dto.UpdateCommentDto;
import mainservice.comment.mapper.CommentMapper;
import mainservice.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class PrivateCommentController {

    private final CommentService commentService;

    @Autowired
    public PrivateCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{userId}/comments")
    public CommentDto createComment(@PathVariable Long userId,
                                    @RequestBody NewCommentDto newCommentDto) {
        log.info("Has received request to endpoint POST/users/{}/comments", userId);
        return CommentMapper.toCommentDto(commentService.createComment(userId, newCommentDto));
    }

    @PatchMapping("/{userId}/comments")
    public CommentDto updateCommentByUser(@PathVariable Long userId,
                                          @RequestBody UpdateCommentDto updateCommentDto) {
        log.info("Has received request to endpoint PATCH/users/{}/comments", userId);
        return CommentMapper.toCommentDto(commentService.updateCommentByUser(userId, updateCommentDto));
    }

    @GetMapping("/{userId}/comments")
    public Collection<CommentDto> getAllCommentsByUser(@PathVariable Long userId,
                                                       @RequestParam(defaultValue = "0") Integer from,
                                                       @RequestParam(defaultValue = "10") Integer size) {
        log.info("Has received request to endpoint GET/users/{}/comments?from={}size={}",
                userId, from, size);
        return commentService.getAllCommentsByUser(userId, from, size);
    }

    @GetMapping("/{userId}/comments/{commentId}")
    public CommentDto getCommentById(@PathVariable Long userId,
                                     @PathVariable Long commentId) {
        log.info("Has received request to endpoint GET/users/{}/comments/{}", userId, commentId);
        return CommentMapper.toCommentDto(commentService.getCommentById(commentId));
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    public void deleteCommentByUser(@PathVariable Long userId,
                                    @PathVariable Long commentId) {
        log.info("Has received request to endpoint DELETE/users/{}/comments/{}", userId, commentId);
        commentService.deleteCommentByUser(userId, commentId);
    }

}
