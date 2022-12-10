package mainservice.comment.service;


import mainservice.comment.dto.CommentDto;
import mainservice.comment.dto.NewCommentDto;
import mainservice.comment.dto.UpdateCommentDto;
import mainservice.comment.model.Comment;

import java.util.Collection;

public interface CommentService {

    Comment createComment(Long userId, NewCommentDto newCommentDto);

    Comment updateCommentByAdmin(UpdateCommentDto updateCommentDto);

    Comment updateCommentByUser(Long userId, UpdateCommentDto updateCommentDto);

    Comment getCommentById(Long commId);


    Collection<Comment> getAllCommentsByEvent(Long eventId, Integer from, Integer size);

    Collection<CommentDto> getAllCommentsByUser(Long userId, Integer from, Integer size);

    void deleteCommentByAdmin(Long commId);

    void deleteCommentByUser(Long userId, Long commId);
}
