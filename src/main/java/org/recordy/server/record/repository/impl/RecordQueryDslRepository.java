package org.recordy.server.record.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.keyword.domain.KeywordEntity;
import org.recordy.server.record.domain.RecordEntity;
import org.recordy.server.user.domain.QUserEntity;
import org.recordy.server.user.domain.UserEntity;
import org.recordy.server.user.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.recordy.server.keyword.domain.QKeywordEntity.keywordEntity;
import static org.recordy.server.record.domain.QRecordEntity.recordEntity;
import static org.recordy.server.record.domain.QUploadEntity.uploadEntity;
import static org.recordy.server.record_stat.domain.QBookmarkEntity.bookmarkEntity;
import static org.recordy.server.record_stat.domain.QViewEntity.viewEntity;

@RequiredArgsConstructor
@Repository
public class RecordQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<RecordEntity> findAllOrderByPopularity(int limit) {
        return jpaQueryFactory
                .selectFrom(recordEntity)
                .leftJoin(recordEntity.bookmarks, bookmarkEntity)
                .leftJoin(recordEntity.views, viewEntity)
                .groupBy(recordEntity.id)
                .orderBy(bookmarkEntity.count().multiply(2).add(viewEntity.count()).desc())
                .limit(limit)
                .fetch();
    }

    public Slice<RecordEntity> findAllByIdAfterOrderByIdDesc(long cursor, Pageable pageable) {
        // TODO: 0을 여기서 대체하지 말고, 서비스나 컨트롤러에서 처리하도록 수정
        if (cursor == 0)
            cursor = Long.MAX_VALUE;

        List<RecordEntity> recordEntities = jpaQueryFactory
                .selectFrom(recordEntity)
                .where(
                        QueryDslUtils.ltCursorId(cursor, recordEntity.id)
                )
                .orderBy(recordEntity.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(recordEntities, pageable, QueryDslUtils.hasNext(pageable, recordEntities));
    }

    public Slice<RecordEntity> findAllByIdAfterAndKeywordsOrderByIdDesc(List<KeywordEntity> keywords, long cursor, Pageable pageable) {
        List<RecordEntity> recordEntities = jpaQueryFactory
                .selectFrom(recordEntity)
                .join(recordEntity.uploads, uploadEntity)
                .join(uploadEntity.keyword, keywordEntity)
                .where(
                        QueryDslUtils.ltCursorId(cursor, recordEntity.id),
                        keywordEntity.in(keywords)
                )
                .orderBy(recordEntity.id.desc())
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

        return new SliceImpl<>(recordEntities, pageable, QueryDslUtils.hasNext(pageable, recordEntities));
    }

    public Map<KeywordEntity, Long> countAllUploadsByUserIdGroupByKeyword(long userId) {
        List<Tuple> results = jpaQueryFactory
                .select(uploadEntity.keyword, uploadEntity.count())
                .from(uploadEntity)
                .where(uploadEntity.record.user.id.eq(userId))
                .groupBy(uploadEntity.keyword)
                .fetch();

        return results.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(uploadEntity.keyword),
                        tuple -> tuple.get(viewEntity.count())));
    }
}
