package org.recordy.server.bookmark.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.bookmark.domain.BookmarkEntity;
import org.recordy.server.keyword.domain.KeywordEntity;
import org.recordy.server.user.domain.QUserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import static org.recordy.server.record.domain.QRecordEntity.recordEntity;
import static org.recordy.server.bookmark.domain.QBookmarkEntity.bookmarkEntity;
import static org.recordy.server.record.domain.QUploadEntity.uploadEntity;

@RequiredArgsConstructor
@Repository
public class BookmarkQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Slice<BookmarkEntity> findAllByUserOrderByIdDesc(long userId, Long cursor, Pageable pageable) {
        List<BookmarkEntity> bookmarkEntities = jpaQueryFactory
                .selectFrom(bookmarkEntity)
                .join(bookmarkEntity.record, recordEntity)
                .join(bookmarkEntity.user, QUserEntity.userEntity)
                .where(
                        QueryDslUtils.ltCursorId(cursor, bookmarkEntity.id),
                        QUserEntity.userEntity.id.eq(userId)
                )
                .orderBy(bookmarkEntity.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(bookmarkEntities, pageable, QueryDslUtils.hasNext(pageable, bookmarkEntities));
    }

    public Map<KeywordEntity, Long> countAllByUserIdGroupByKeyword(long userId) {
        List<Tuple> result = jpaQueryFactory
                .select(uploadEntity.keyword, bookmarkEntity.count())
                .from(bookmarkEntity)
                .join(bookmarkEntity.record, recordEntity)
                .join(recordEntity.uploads, uploadEntity)
                .where(bookmarkEntity.user.id.eq(userId))
                .groupBy(uploadEntity.keyword)
                .fetch();

        return result.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(uploadEntity.keyword),
                        tuple -> tuple.get(bookmarkEntity.count())
                ));
    }
}
