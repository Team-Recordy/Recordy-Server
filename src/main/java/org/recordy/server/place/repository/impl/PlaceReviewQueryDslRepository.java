package org.recordy.server.place.repository.impl;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.recordy.server.place.controller.dto.response.PlaceReviewGetResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.recordy.server.place.domain.QPlaceEntity.placeEntity;
import static org.recordy.server.place.domain.QPlaceReview.placeReview;

@RequiredArgsConstructor
@Repository
public class PlaceReviewQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private static final ConstructorExpression<PlaceReviewGetResponse> placeReviewGetResponse = Projections.constructor(
            PlaceReviewGetResponse.class,
            placeReview.id,
            placeReview.authorName,
            placeReview.content,
            placeReview.rating,
            placeReview.writtenAt
    );

    public List<PlaceReviewGetResponse> findAllByPlaceId(long placeId) {
        return jpaQueryFactory
                .select(placeReviewGetResponse)
                .from(placeReview)
                .join(placeReview.place, placeEntity)
                .where(placeEntity.id.eq(placeId))
                .orderBy(placeReview.writtenAt.desc())
                .fetch();
    }
}
