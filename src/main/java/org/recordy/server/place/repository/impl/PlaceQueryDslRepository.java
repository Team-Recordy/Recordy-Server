package org.recordy.server.place.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.place.domain.PlaceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static org.recordy.server.exhibition.domain.QExhibitionEntity.exhibitionEntity;
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

    public Slice<PlaceEntity> findAllOrderByExhibitionStartDateDesc(Pageable pageable) {
        List<PlaceEntity> content = jpaQueryFactory
                .selectFrom(placeEntity)
                .join(placeEntity.exhibitions, exhibitionEntity).fetchJoin()
                .where(
                        exhibitionEntity.endDate.goe(LocalDate.now(Clock.systemDefaultZone()))
                )
                .orderBy(exhibitionEntity.startDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(content, pageable, QueryDslUtils.hasNext(pageable, content));
    }
}
