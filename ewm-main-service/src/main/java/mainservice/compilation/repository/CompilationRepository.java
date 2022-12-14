package mainservice.compilation.repository;

import mainservice.compilation.model.Compilation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("SELECT c FROM Compilation c WHERE c.pinned = :pinned")
    List<Compilation> findAllByPinnedIsTrue(Boolean pinned, Pageable pageable);
}
