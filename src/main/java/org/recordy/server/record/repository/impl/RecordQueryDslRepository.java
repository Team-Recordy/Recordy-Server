package org.recordy.server.record.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.keyword.domain.KeywordEntity;
import org.recordy.server.keyword.domain.QKeywordEntity;
import org.recordy.server.record.domain.QRecordEntity;
import org.recordy.server.record.domain.QUploadEntity;
import org.recordy.server.record.domain.RecordEntity;
import org.recordy.server.user.domain.QUserEntity;
import org.recordy.server.user.domain.UserEntity;
import org.recordy.server.user.repository.UserRepository;
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
        // TODO: 0을 여기서 대체하지 말고, 서비스나 컨트롤러에서 처리하도록 수정
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

    public Slice<RecordEntity> findAllByIdAfterAndKeywordsOrderByIdDesc(List<KeywordEntity> keywords, long cursor, Pageable pageable) {
        List<RecordEntity> recordEntities = jpaQueryFactory
                .selectFrom(QRecordEntity.recordEntity)
                .join(QRecordEntity.recordEntity.uploads, QUploadEntity.uploadEntity)
                .join(QUploadEntity.uploadEntity.keyword, QKeywordEntity.keywordEntity)
                .where(
                        QueryDslUtils.ltCursorId(cursor, QRecordEntity.recordEntity.id),
                        QKeywordEntity.keywordEntity.in(keywords)
                )
                .orderBy(QRecordEntity.recordEntity.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(recordEntities, pageable, QueryDslUtils.hasNext(pageable, recordEntities));
    }

    public Slice<RecordEntity> findAllByUserIdOrderByIdDesc(UserEntity userEntity, long cursor, Pageable pageable) {
        List<RecordEntity> recordEntities = jpaQueryFactory
                .selectFrom((QRecordEntity.recordEntity))
                .join(QRecordEntity.recordEntity.user, QUserEntity.userEntity)
                .where(
                        QueryDslUtils.ltCursorId(cursor,QRecordEntity.recordEntity.id),
                        QUserEntity.userEntity.eq(userEntity)
                )
                .orderBy(QRecordEntity.recordEntity.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(recordEntities, pageable, QueryDslUtils.hasNext(pageable,recordEntities));
    }
}
