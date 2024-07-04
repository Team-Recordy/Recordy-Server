package org.recordy.server.record.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.record.domain.QRecordEntity;
import org.recordy.server.record.domain.RecordEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class RecordQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Slice<RecordEntity> findAllByIdAfterOrderByIdDesc(long cursor, Pageable pageable) {
        if (cursor == 0)
            cursor = Long.MAX_VALUE;

        List<RecordEntity> recordEntities = jpaQueryFactory
                .selectFrom(QRecordEntity.recordEntity)
                .where(
                        QueryDslUtils.ltCursorId(cursor, QRecordEntity.recordEntity.id)
                )
                .orderBy(QRecordEntity.recordEntity.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(recordEntities, pageable, QueryDslUtils.hasNext(pageable, recordEntities));
    }
}
