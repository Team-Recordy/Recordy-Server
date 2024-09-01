package org.recordy.server.bookmark.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.bookmark.domain.BookmarkEntity;
import org.recordy.server.user.domain.QUserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import static org.recordy.server.record.domain.QRecordEntity.recordEntity;
import static org.recordy.server.bookmark.domain.QBookmarkEntity.bookmarkEntity;

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
}
