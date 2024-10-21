package org.recordy.server.place.repository;

import org.recordy.server.place.controller.dto.response.PlaceReviewGetResponse;
import org.recordy.server.place.domain.PlaceReview;

import java.util.List;

public interface PlaceReviewRepository {

    // command
    void saveAll(List<PlaceReview> reviews);

    // query
    List<PlaceReviewGetResponse> findAllByPlaceId(long placeId);
}
