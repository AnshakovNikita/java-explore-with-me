package mainservice.comment.mapper;

import lombok.Data;
import mainservice.comment.dto.CommentDto;
import mainservice.comment.dto.NewCommentDto;
import mainservice.comment.model.Comment;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
@Component
public class CommentMapper {

    public static Collection<CommentDto> toCommentDtoCollection(Collection<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .event(comment.getEvent().getId())
                .author(comment.getAuthor().getId())
                .created(comment.getCreated())
                .build();
    }

    public static Comment toComment(NewCommentDto newCommentDto) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .build();
    }
}
