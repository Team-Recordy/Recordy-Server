package org.recordy.server.record.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.recordy.server.keyword.domain.KeywordEntity;
import org.recordy.server.record.domain.UploadEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.recordy.server.record.domain.QRecordEntity.recordEntity;
import static org.recordy.server.record.domain.QUploadEntity.uploadEntity;

@RequiredArgsConstructor
@Repository
public class UploadQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<UploadEntity> findAllByRecordId(long recordId) {
        return jpaQueryFactory
                .selectFrom(uploadEntity)
                .where(uploadEntity.record.id.eq(recordId))
                .fetch();
    }

    public Map<KeywordEntity, Long> countAllByUserIdGroupByKeyword(long userId) {
        List<Tuple> result = jpaQueryFactory
                .select(uploadEntity.keyword, uploadEntity.count())
                .from(uploadEntity)
                .join(uploadEntity.record, recordEntity)
                .where(recordEntity.user.id.eq(userId))
                .groupBy(uploadEntity.keyword)
                .fetch();

        return result.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(uploadEntity.keyword),
                        tuple -> tuple.get(uploadEntity.count())));
    }
}
