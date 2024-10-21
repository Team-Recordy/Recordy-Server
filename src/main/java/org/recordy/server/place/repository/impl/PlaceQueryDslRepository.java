package org.recordy.server.place.repository.impl;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.spatial.locationtech.jts.JTSGeometryExpressions;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.location.controller.dto.response.LocationGetResponse;
import org.recordy.server.place.controller.dto.response.PlaceGetResponse;
import org.recordy.server.place.domain.PlaceEntity;
import org.recordy.server.place.exception.PlaceException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static org.recordy.server.common.util.QueryDslUtils.hasOngoingExhibitions;
import static org.recordy.server.exhibition.domain.QExhibitionEntity.exhibitionEntity;
import static org.recordy.server.location.domain.QLocationEntity.locationEntity;
import static org.recordy.server.place.domain.QPlaceEntity.placeEntity;
import static org.recordy.server.record.domain.QRecordEntity.recordEntity;

@RequiredArgsConstructor
@Repository
public class PlaceQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private static final ConstructorExpression<LocationGetResponse> locationGetResponse = Projections.constructor(
            LocationGetResponse.class,
            placeEntity.location.id,
            placeEntity.location.geometry
    );
    private static final ConstructorExpression<PlaceGetResponse> placeGetResponse = Projections.constructor(
            PlaceGetResponse.class,
            placeEntity.id,
            placeEntity.name,
            placeEntity.address,
            placeEntity.platformId,
            locationGetResponse
    );

    public PlaceEntity findById(long id) {
        return jpaQueryFactory
                .select(placeEntity)
                .from(placeEntity)
                .join(placeEntity.location).fetchJoin()
                .leftJoin(placeEntity.exhibitions, exhibitionEntity).fetchJoin()
                .where(placeEntity.id.eq(id))
                .fetchOne();
    }

    public Long findByName(String name) {
        return findIdWith(placeEntity.name.eq(name));
    }

    private Long findIdWith(BooleanExpression... expressions) {
        return jpaQueryFactory
                .select(placeEntity.id)
                .from(placeEntity)
                .join(placeEntity.location)
                .leftJoin(placeEntity.exhibitions, exhibitionEntity)
                .where(expressions)
                .fetchOne();
    }

    public PlaceGetResponse findById(Long id) {
        PlaceGetResponse place = Optional.ofNullable(jpaQueryFactory
                .select(placeGetResponse)
                .from(placeEntity)
                .join(placeEntity.location, locationEntity)
                .where(placeEntity.id.eq(id))
                .fetchOne())
                .orElseThrow(() -> new PlaceException(ErrorMessage.PLACE_NOT_FOUND));

        Long exhibitionSize = Optional.ofNullable(jpaQueryFactory
                .select(exhibitionEntity.count())
                .from(placeEntity)
                .leftJoin(placeEntity.exhibitions, exhibitionEntity)
                .where(placeEntity.id.eq(id))
                .fetchOne())
                .orElse(0L);
        place.setExhibitionSize(exhibitionSize);

        Long recordSize = Optional.ofNullable(jpaQueryFactory
                .select(recordEntity.count())
                .from(placeEntity)
                .leftJoin(recordEntity).on(recordEntity.place.id.eq(id))
                .where(placeEntity.id.eq(id))
                .fetchOne())
                .orElse(0L);
        place.setRecordSize(recordSize);

        return place;
    }

    public Slice<PlaceGetResponse> findAllOrderByExhibitionStartDateDesc(Pageable pageable) {
        List<PlaceGetResponse> content = findPlacesWith(pageable);

        collectExhibitionCounts(content);
        return new SliceImpl<>(content, pageable, QueryDslUtils.hasNext(pageable, content));
    }

    public Slice<PlaceGetResponse> findAllByLocationOrderByExhibitionStartDateDesc(Pageable pageable, Point currentLocation, double distance) {
        List<PlaceGetResponse> content = findPlacesWith(
                pageable,
                JTSGeometryExpressions
                        .asJTSGeometry(currentLocation)
                        .buffer(distance)
                        .contains(locationEntity.geometry)
        );

        System.out.println("content = " + content);
        collectExhibitionCounts(content);
        return new SliceImpl<>(content, pageable, QueryDslUtils.hasNext(pageable, content));
    }

    private List<PlaceGetResponse> findPlacesWith(Pageable pageable, BooleanExpression... expressions) {
        return jpaQueryFactory
                .from(placeEntity)
                .join(placeEntity.location, locationEntity)
                .leftJoin(placeEntity.exhibitions, exhibitionEntity)
                .where(expressions)
                .where(hasOngoingExhibitions)
                .orderBy(exhibitionEntity.startDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .transform(groupBy(placeEntity.id).list(placeGetResponse));
    }

    private void collectExhibitionCounts(List<PlaceGetResponse> places) {
        Map<Long, Long> exhibitionSizes = jpaQueryFactory
                .select(placeEntity.id, exhibitionEntity.count())
                .from(placeEntity)
                .leftJoin(placeEntity.exhibitions, exhibitionEntity)
                .where(placeEntity.id.in(places.stream().map(PlaceGetResponse::getId).toList()))
                .where(hasOngoingExhibitions)
                .groupBy(placeEntity.id)
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(placeEntity.id),
                        tuple -> Optional.ofNullable(tuple.get(exhibitionEntity.count())).orElse(0L)
                ));

        for (PlaceGetResponse place : places) {
            Long exhibitionSize = exhibitionSizes.getOrDefault(place.getId(), 0L);
            place.setExhibitionSize(exhibitionSize);
        }
    }
}
