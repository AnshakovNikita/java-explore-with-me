package stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import stats.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {

    @Query(value = "select app, uri, count(distinct ip) hits " +
            " from stats " +
            " where timestamp between ?1 and ?2" +
            "       and uri in ?3" +
            " group by app, uri",
            nativeQuery = true)
    List<Object[]> getEndpointHitsUnique(LocalDateTime startFormatted, LocalDateTime endFormatted,
                                         List<String> uris);

    @Query(value = "select app, uri, count(ip) hits " +
            " from stats " +
            " where timestamp between ?1 and ?2" +
            "       and uri in ?3" +
            " group by app, uri",
            nativeQuery = true)
    List<Object[]> getEndpointHitsNotUnique(LocalDateTime startFormatted, LocalDateTime endFormatted,
                                            List<String> uris);
}
