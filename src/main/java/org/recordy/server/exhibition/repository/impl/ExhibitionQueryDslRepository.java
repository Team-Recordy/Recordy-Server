package org.recordy.server.exhibition.repository.impl;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.exhibition.controller.dto.response.ExhibitionGetResponse;
import org.recordy.server.exhibition.domain.ExhibitionEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.recordy.server.common.util.QueryDslUtils.hasOngoingExhibitions;
import static org.recordy.server.exhibition.domain.QExhibitionEntity.exhibitionEntity;
import static org.recordy.server.place.domain.QPlaceEntity.placeEntity;

@RequiredArgsConstructor
@Repository
public class ExhibitionQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private static final ConstructorExpression<ExhibitionGetResponse> exhibitionGetResponse = Projections.constructor(
            ExhibitionGetResponse.class,
            exhibitionEntity.id,
            exhibitionEntity.name,
            exhibitionEntity.startDate,
            exhibitionEntity.endDate,
            exhibitionEntity.isFree
    );

    public ExhibitionEntity findById(long id) {
        return jpaQueryFactory
                .selectFrom(exhibitionEntity)
                .join(exhibitionEntity.place).fetchJoin()
                .where(exhibitionEntity.id.eq(id))
                .fetchOne();
    }

    public Slice<ExhibitionEntity> findAllContainingName(String name, Long cursor, int size) {
        List<ExhibitionEntity> content = jpaQueryFactory
                .selectFrom(exhibitionEntity)
                .join(exhibitionEntity.place).fetchJoin()
                .where(
                        exhibitionEntity.name.containsIgnoreCase(name),
                        QueryDslUtils.ltCursorId(cursor, exhibitionEntity.id)
                )
                .orderBy(exhibitionEntity.id.desc())
                .limit(size + 1)
                .fetch();

        return new SliceImpl<>(content, PageRequest.ofSize(size),QueryDslUtils.hasNext(size, content));
    }

    public List<ExhibitionGetResponse> findAllByPlaceId(long placeId) {
        return findAllOngoingWith(
                exhibitionEntity.startDate.desc(),
                placeEntity.id.eq(placeId)
        );
    }

    public List<ExhibitionGetResponse> findAllFreeByPlaceId(long placeId) {
        return findAllOngoingWith(
                exhibitionEntity.startDate.desc(),
                placeEntity.id.eq(placeId),
                exhibitionEntity.isFree.isTrue()
        );
    }

    public List<ExhibitionGetResponse> findAllByPlaceIdOrderByEndDateDesc(long placeId) {
        return findAllOngoingWith(
                exhibitionEntity.endDate.asc(),
                placeEntity.id.eq(placeId)
        );
    }

    private List<ExhibitionGetResponse> findAllOngoingWith(OrderSpecifier order, BooleanExpression... expressions) {
        return jpaQueryFactory
                .select(exhibitionGetResponse)
                .from(exhibitionEntity)
                .join(exhibitionEntity.place, placeEntity)
                .where(expressions)
                .where(hasOngoingExhibitions)
                .orderBy(order)
                .fetch();
    }
}
