package org.recordy.server.place.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.recordy.server.place.domain.PlaceEntity;
import org.springframework.stereotype.Repository;

import static org.recordy.server.place.domain.QPlaceEntity.placeEntity;

@RequiredArgsConstructor
@Repository
public class PlaceQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PlaceEntity findById(long id) {
        return jpaQueryFactory
                .selectFrom(placeEntity)
                .where(placeEntity.id.eq(id))
                .fetchOne();
    }
}
