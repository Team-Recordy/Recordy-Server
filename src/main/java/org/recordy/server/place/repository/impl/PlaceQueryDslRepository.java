package org.recordy.server.place.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.exhibition.domain.ExhibitionEntity;
import org.recordy.server.place.domain.PlaceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.recordy.server.exhibition.domain.QExhibitionEntity.exhibitionEntity;
import static org.recordy.server.place.domain.QPlaceEntity.placeEntity;

@RequiredArgsConstructor
@Repository
public class PlaceQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PlaceEntity findById(long id) {
        PlaceEntity content = jpaQueryFactory
                .selectFrom(placeEntity)
                .join(placeEntity.location).fetchJoin()
                .where(placeEntity.id.eq(id))
                .fetchOne();

        if (Objects.isNull(content)) {
            return null;
        }

        List<ExhibitionEntity> exhibitions = jpaQueryFactory
                .selectFrom(exhibitionEntity)
                .where(exhibitionEntity.place.id.eq(id))
                .fetch();

        return content.with(exhibitions);
    }

    public Slice<PlaceEntity> findAllOrderByExhibitionStartDateDesc(Pageable pageable) {
        Map<Long, List<ExhibitionEntity>> exhibitions = jpaQueryFactory
                .selectFrom(exhibitionEntity)
                .where(
                        exhibitionEntity.endDate.goe(LocalDate.now(Clock.systemDefaultZone())),
                        exhibitionEntity.startDate.loe(LocalDate.now(Clock.systemDefaultZone()))
                )
                .orderBy(exhibitionEntity.startDate.desc())
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(exhibition -> exhibition.getPlace().getId()));

        List<PlaceEntity> content = jpaQueryFactory
                .selectFrom(placeEntity)
                .join(placeEntity.location).fetchJoin()
                .fetch()
                .stream()
                .map(place -> place.with(exhibitions.get(place.getId())))
                .filter(place -> Objects.nonNull(place.getExhibitions()) && !place.getExhibitions().isEmpty())
                .sorted(Comparator.comparing(place -> place.getExhibitions().get(0).getStartDate(), Comparator.reverseOrder()))
                .toList();

        return new SliceImpl<>(content, pageable, QueryDslUtils.hasNext(pageable, content));
    }
}
