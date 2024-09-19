package org.recordy.server.place.repository.impl;

import org.recordy.server.place.domain.PlaceReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceReviewJpaRepository extends JpaRepository<PlaceReview, Long> {
}
