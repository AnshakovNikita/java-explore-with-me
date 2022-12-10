package mainservice.comment.repository;

import mainservice.comment.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Override
    <S extends Comment> S save(S comment);

    @Query("select c " +
            "from Comment c " +
            "where c.author.id = ?1 " +
            "group by c.id, c.text, c.author.id, c.created, c.event.id " +
            "order by c.created desc")
    Page<Comment> getAllCommentsByUser(Long userId, Pageable pageable);

    @Query("select c " +
            "from Comment c " +
            "where c.event.id = ?1 " +
            "group by c.id, c.text, c.author.id, c.created, c.event.id " +
            "order by c.created desc")
    Page<Comment> getAllCommentsByEvent(Long eventId, Pageable pageable);
}
