package org.recordy.server.place.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.recordy.server.place.service.dto.Review;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class PlaceReview {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authorName;
    @Column(length = 2000)
    private String content;
    private int rating;
    private LocalDateTime writtenAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private PlaceEntity place;

    private PlaceReview(
        String authorName,
        String content,
        int rating,
        LocalDateTime writtenAt,
        PlaceEntity place
    ) {
        this.authorName = authorName;
        this.content = content;
        this.rating = rating;
        this.writtenAt = writtenAt;
        this.place = place;
    }

    public static PlaceReview of(
            Review review,
            PlaceEntity place
    ) {
        return new PlaceReview(
            review.author_name(),
            review.text(),
            review.rating(),
            LocalDateTime.now(),
            place
        );
    }
}
