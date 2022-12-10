package mainservice.controllers.admin;

import lombok.extern.slf4j.Slf4j;
import mainservice.comment.dto.CommentDto;
import mainservice.comment.dto.UpdateCommentDto;
import mainservice.comment.mapper.CommentMapper;
import mainservice.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/admin/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{commentId}")
    public CommentDto getCommentById(@PathVariable Long commentId) {
        log.info("Has received request to endpoint GET/admin/comments/{}", commentId);
        return CommentMapper.toCommentDto(commentService.getCommentById(commentId));
    }

    @GetMapping("/all/{userId}")
    public Collection<CommentDto> getAllCommentsByUser(@PathVariable Long userId,
                                                       @RequestParam(defaultValue = "0") Integer from,
                                                       @RequestParam(defaultValue = "10") Integer size) {
        log.info("Has received request to endpoint GET/users/{}/comments?from={}size={}",
                userId, from, size);
        return commentService.getAllCommentsByUser(userId, from, size);
    }

    @PutMapping("/{commentId}")
    public CommentDto updateCommentByAdmin(@PathVariable Long commentId,
                                           @RequestBody UpdateCommentDto updateCommentDto) {
        log.info("Has received request to endpoint PUT/admin/comments/{}", commentId);
        return CommentMapper.toCommentDto(commentService.updateCommentByAdmin(updateCommentDto));
    }

    @DeleteMapping("/{commentId}")
    public void deleteCommentByAdmin(@PathVariable Long commentId) {
        log.info("Has received request to endpoint DELETE/admin/comments/{}", commentId);
        commentService.deleteCommentByAdmin(commentId);
    }

}
