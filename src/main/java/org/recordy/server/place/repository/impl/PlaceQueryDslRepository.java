package org.recordy.server.place.repository.impl;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.spatial.locationtech.jts.JTSGeometryExpressions;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.location.controller.dto.response.LocationGetResponse;
import org.recordy.server.place.controller.dto.response.PlaceGetResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static org.recordy.server.exhibition.domain.QExhibitionEntity.exhibitionEntity;
import static org.recordy.server.location.domain.QLocationEntity.locationEntity;
import static org.recordy.server.place.domain.QPlaceEntity.placeEntity;

@RequiredArgsConstructor
@Repository
public class PlaceQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private static final ConstructorExpression<LocationGetResponse> locationGetResponse = Projections.constructor(
            LocationGetResponse.class,
            placeEntity.location.id,
            placeEntity.location.geometry,
            placeEntity.location.address
    );
    private static final ConstructorExpression<PlaceGetResponse> placeGetResponse = Projections.constructor(
            PlaceGetResponse.class,
            placeEntity.id,
            placeEntity.name,
            locationGetResponse
    );

    public Long findById(long id) {
        return findIdWith(placeEntity.id.eq(id));
    }

    public Long findByName(String name) {
        return findIdWith(placeEntity.name.eq(name));
    }

    private Long findIdWith(BooleanExpression... expressions) {
        return jpaQueryFactory
                .select(placeEntity.id)
                .from(placeEntity)
                .join(placeEntity.location)
                .join(placeEntity.exhibitions, exhibitionEntity)
                .where(expressions)
                .fetchOne();
    }

    public Slice<PlaceGetResponse> findAllOrderByExhibitionStartDateDesc(Pageable pageable) {
        List<PlaceGetResponse> content = findPlacesWith(pageable);

        return new SliceImpl<>(content, pageable, QueryDslUtils.hasNext(pageable, content));
    }

    public Slice<PlaceGetResponse> findAllFreeOrderByExhibitionStartDateDesc(Pageable pageable) {
        List<PlaceGetResponse> content = findPlacesWith(
                pageable,
                exhibitionEntity.isFree.isTrue()
        );

        return new SliceImpl<>(content, pageable, QueryDslUtils.hasNext(pageable, content));
    }

    public Slice<PlaceGetResponse> findAllByNameOrderByExhibitionStartDateDesc(Pageable pageable, String query) {
        List<PlaceGetResponse> content = findPlacesWith(
                pageable,
                placeEntity.name.containsIgnoreCase(query)
        );

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

        return new SliceImpl<>(content, pageable, QueryDslUtils.hasNext(pageable, content));
    }

    private List<PlaceGetResponse> findPlacesWith(Pageable pageable, BooleanExpression... expressions) {
        Predicate[] hasOngoingExhibitions = {
                exhibitionEntity.endDate.goe(LocalDate.now(Clock.systemDefaultZone())),
                exhibitionEntity.startDate.loe(LocalDate.now(Clock.systemDefaultZone()))
        };

        List<PlaceGetResponse> places = jpaQueryFactory
                .from(placeEntity)
                .join(placeEntity.location, locationEntity)
                .leftJoin(placeEntity.exhibitions, exhibitionEntity)
                .where(expressions)
                .where(hasOngoingExhibitions)
                .orderBy(exhibitionEntity.startDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(groupBy(placeEntity.id).list(placeGetResponse));

        collectExhibitionCounts(places, hasOngoingExhibitions);
        return places;
    }

    private void collectExhibitionCounts(List<PlaceGetResponse> places, Predicate[] hasOngoingExhibitions) {
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
