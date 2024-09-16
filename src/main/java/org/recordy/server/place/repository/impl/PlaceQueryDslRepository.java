package org.recordy.server.place.repository.impl;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.spatial.locationtech.jts.JTSGeometryExpressions;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.exhibition.controller.dto.response.ExhibitionGetResponse;
import org.recordy.server.exhibition.domain.ExhibitionEntity;
import org.recordy.server.location.controller.dto.response.LocationGetResponse;
import org.recordy.server.place.controller.dto.response.PlaceGetResponse;
import org.recordy.server.place.domain.PlaceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static org.recordy.server.exhibition.domain.QExhibitionEntity.exhibitionEntity;
import static org.recordy.server.location.domain.QLocationEntity.locationEntity;
import static org.recordy.server.place.domain.QPlaceEntity.placeEntity;

@RequiredArgsConstructor
@Repository
public class PlaceQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private static final ConstructorExpression<ExhibitionGetResponse> exhibitionGetResponse = Projections.constructor(
            ExhibitionGetResponse.class,
            exhibitionEntity.id,
            exhibitionEntity.name,
            exhibitionEntity.startDate,
            exhibitionEntity.endDate,
            exhibitionEntity.isFree
    );
    private static final ConstructorExpression<LocationGetResponse> locationGetResponse = Projections.constructor(
            LocationGetResponse.class,
            placeEntity.location.id,
            placeEntity.location.geometry,
            placeEntity.location.address.formatted,
            placeEntity.location.address.sido,
            placeEntity.location.address.gugun
    );
    private static final ConstructorExpression<PlaceGetResponse> placeGetResponse = Projections.constructor(
            PlaceGetResponse.class,
            placeEntity.id,
            placeEntity.name,
            list(exhibitionGetResponse),
            locationGetResponse
    );

    public PlaceEntity findById(long id) {
        List<PlaceEntity> content = jpaQueryFactory
                .from(placeEntity)
                .join(placeEntity.location)
                .join(placeEntity.exhibitions, exhibitionEntity)
                .where(placeEntity.id.eq(id))
                .transform(groupBy(placeEntity.id).list(
                        Projections.constructor(
                                PlaceEntity.class,
                                placeEntity.id,
                                placeEntity.name,
                                list(Projections.constructor(
                                        ExhibitionEntity.class,
                                        exhibitionEntity.id,
                                        exhibitionEntity.name,
                                        exhibitionEntity.startDate,
                                        exhibitionEntity.endDate,
                                        exhibitionEntity.isFree,
                                        exhibitionEntity.url,
                                        exhibitionEntity.place
                                )),
                                placeEntity.location
                        )
                ));

        if (content.isEmpty()) {
            return null;
        }

        return content.get(0);
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
        return jpaQueryFactory
                .from(placeEntity)
                .join(placeEntity.location, locationEntity)
                .leftJoin(placeEntity.exhibitions, exhibitionEntity)
                .where(expressions)
                .where(
                        exhibitionEntity.endDate.goe(LocalDate.now(Clock.systemDefaultZone())),
                        exhibitionEntity.startDate.loe(LocalDate.now(Clock.systemDefaultZone()))
                )
                .orderBy(exhibitionEntity.startDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(groupBy(placeEntity.id).list(placeGetResponse));
    }
}
