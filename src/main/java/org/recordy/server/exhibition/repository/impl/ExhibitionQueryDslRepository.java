package org.recordy.server.exhibition.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.exhibition.domain.ExhibitionEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.recordy.server.exhibition.domain.QExhibitionEntity.exhibitionEntity;

@RequiredArgsConstructor
@Repository
public class ExhibitionQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public ExhibitionEntity findById(long id) {
        return jpaQueryFactory
                .selectFrom(exhibitionEntity)
                .where(exhibitionEntity.id.eq(id))
                .fetchOne();
    }

    public Slice<ExhibitionEntity> findAllContainingName(String name, Long cursor, int size) {
        List<ExhibitionEntity> content = jpaQueryFactory
                .selectFrom(exhibitionEntity)
                .where(
                        exhibitionEntity.name.containsIgnoreCase(name),
                        QueryDslUtils.ltCursorId(cursor, exhibitionEntity.id)
                )
                .orderBy(exhibitionEntity.id.desc())
                .limit(size + 1)
                .fetch();

        return new SliceImpl<>(content, PageRequest.ofSize(size),QueryDslUtils.hasNext(size, content));
    }
}
