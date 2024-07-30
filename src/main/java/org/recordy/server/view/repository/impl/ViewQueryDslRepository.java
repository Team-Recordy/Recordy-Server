package org.recordy.server.view.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.recordy.server.keyword.domain.KeywordEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.recordy.server.record.domain.QRecordEntity.recordEntity;
import static org.recordy.server.record.domain.QUploadEntity.uploadEntity;
import static org.recordy.server.view.domain.QViewEntity.viewEntity;

@RequiredArgsConstructor
@Repository
public class ViewQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Map<KeywordEntity, Long> countAllByUserIdGroupByKeyword(long userId) {
        List<Tuple> result = jpaQueryFactory
                .select(uploadEntity.keyword, viewEntity.count())
                .from(viewEntity)
                .join(viewEntity.record, recordEntity)
                .join(recordEntity.uploads, uploadEntity)
                .where(viewEntity.user.id.eq(userId))
                .groupBy(uploadEntity.keyword)
                .fetch();

        return result.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(uploadEntity.keyword),
                        tuple -> tuple.get(viewEntity.count())));
    }
}
