package mainservice.event.model;

import lombok.*;
import mainservice.category.model.Category;
import mainservice.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false, length = 120)
    private String title;

    @Column(name = "annotation", nullable = false, length = 2000)
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "event_date", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Column(name = "description", nullable = false, length = 7000)
    private String description;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private EventStatus state;

    @Column(name = "published_on", columnDefinition = "TIMESTAMP")
    private LocalDateTime publishedOn;

    @Column(name = "created_on", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdOn;

    @Column(name = "lat", nullable = false)
    private Float lat;

    @Column(name = "lon", nullable = false)
    private Float lon;

    @Column(name = "request_moderation")
    private Boolean requestModeration;
}