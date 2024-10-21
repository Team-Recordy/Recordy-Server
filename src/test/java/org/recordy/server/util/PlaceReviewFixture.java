package org.recordy.server.util;

import org.recordy.server.place.domain.Place;
import org.recordy.server.place.domain.PlaceEntity;
import org.recordy.server.place.domain.PlaceReview;
import org.recordy.server.place.service.dto.Review;

import java.time.Instant;

public class PlaceReviewFixture {

    public static final String authorName = "konu";
    public static final String content = "awesome";
    public static final int rating = 5;
    public static final long time = Instant.now().getEpochSecond();
    public static final Review review = new Review(authorName, content, rating, time);

    public static PlaceReview create(Place place) {
        return PlaceReview.of(review, PlaceEntity.from(place));
    }
}
