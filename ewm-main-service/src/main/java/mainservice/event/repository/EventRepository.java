package mainservice.event.repository;

import mainservice.event.model.Event;
import mainservice.event.model.EventStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByInitiatorId(Long userId, Pageable page);

    @Query("SELECT e FROM Event e " +
            "WHERE e.state = mainservice.event.model.EventStatus.PUBLISHED " +
            "AND (e.annotation LIKE CONCAT('%',?1,'%') OR e.description LIKE CONCAT('%',?1,'%')) " +
            "AND e.category.id IN ?2 " +
            "AND e.paid = ?3 " +
            "AND e.eventDate BETWEEN ?4 AND ?5 " +
            "AND ((?6 = true AND e.participantLimit = 0) " +
            "OR (?6 = true AND e.participantLimit > e.confirmedRequests) " +
            "OR (?6 = false))")
    List<Event> findEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                           LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (e.category.id IN :categories) AND " +
            "(LOWER(e.annotation) LIKE CONCAT('%',LOWER(:text),'%') OR " +
            "LOWER(e.description) LIKE CONCAT('%',LOWER(:text),'%'))")
    List<Event> findByCategoryIdsAndText(String text, List<Long> categories);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (LOWER(e.annotation) LIKE CONCAT('%',LOWER(:text),'%') OR " +
            "LOWER(e.description) LIKE CONCAT('%',LOWER(:text),'%'))")
    List<Event> findByText(String text);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (e.category.id IN :categories) AND " +
            "(e.initiator.id IN :users) AND " +
            "(e.state IN :statusList)")
    List<Event> findByUsersAndCategoriesAndStates(List<Long> users, List<Long> categories,
                                                  List<EventStatus> statusList, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (e.initiator.id IN :users) AND " +
            "(e.state IN :statusList)")
    List<Event> findByUsersAndStates(List<Long> users, List<EventStatus> statusList, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (e.category.id IN :categories) AND " +
            "(e.state IN :statusList)")
    List<Event> findByCategoriesAndStates(List<Long> categories, List<EventStatus> statusList, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (e.state IN :statusList)")
    List<Event> findByStates(List<EventStatus> statusList, Pageable pageable);
}
