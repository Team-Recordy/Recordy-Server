package org.recordy.server.record.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.recordy.server.record.domain.UploadEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
