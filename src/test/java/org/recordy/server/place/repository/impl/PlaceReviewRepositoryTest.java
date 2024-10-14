package org.recordy.server.place.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.place.controller.dto.response.PlaceReviewGetResponse;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.domain.PlaceReview;
import org.recordy.server.place.repository.PlaceRepository;
import org.recordy.server.place.repository.PlaceReviewRepository;
import org.recordy.server.util.PlaceFixture;
import org.recordy.server.util.PlaceReviewFixture;
import org.recordy.server.util.db.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
class PlaceReviewRepositoryTest extends IntegrationTest {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PlaceReviewRepository placeReviewRepository;

    private Place place;

    @BeforeEach
    void setUp() {
        place = placeRepository.save(PlaceFixture.create());
    }

    @Test
    void 장소와_관련된_장소_리뷰를_리스트로_조회할_수_있다() {
        // given
        int reviewSize = 10;
        List<PlaceReview> reviews = new ArrayList<>();

        for (int i = 0; i < reviewSize; i++) {
            reviews.add(PlaceReviewFixture.create(place));
        }
        placeReviewRepository.saveAll(reviews);

        // when
        List<PlaceReviewGetResponse> result = placeReviewRepository.findAllByPlaceId(place.getId());

        // then
        assertAll(
                () -> assertThat(result).hasSize(reviewSize),
                () -> assertThat(result.stream().map(PlaceReviewGetResponse::authorName)).allMatch(name -> name.equals(PlaceReviewFixture.authorName))
        );
    }
}