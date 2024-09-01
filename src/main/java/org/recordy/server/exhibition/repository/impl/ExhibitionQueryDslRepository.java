package org.recordy.server.exhibition.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.recordy.server.exhibition.domain.ExhibitionEntity;
import org.springframework.stereotype.Repository;

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
}
