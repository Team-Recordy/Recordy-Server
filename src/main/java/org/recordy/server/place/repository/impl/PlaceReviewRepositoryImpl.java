package org.recordy.server.place.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.place.domain.PlaceReview;
import org.recordy.server.place.repository.PlaceReviewRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Repository
public class PlaceReviewRepositoryImpl implements PlaceReviewRepository {

    private final PlaceReviewJpaRepository placeReviewJpaRepository;

    @Override
    public void saveAll(List<PlaceReview> reviews) {
        placeReviewJpaRepository.saveAll(reviews);
    }
}
