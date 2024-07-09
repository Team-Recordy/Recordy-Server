package org.recordy.server.record_stat.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.record.domain.QRecordEntity;
import org.recordy.server.record_stat.domain.BookmarkEntity;
import org.recordy.server.record_stat.domain.QBookmarkEntity;
import org.recordy.server.user.domain.QUserEntity;
import org.recordy.server.user.domain.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BookmarkQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Slice<BookmarkEntity> findAllByUserOrderByIdDesc(UserEntity userEntity, long cursor, Pageable pageable) {
        List<BookmarkEntity> bookmarkEntities = jpaQueryFactory
                .selectFrom(QBookmarkEntity.bookmarkEntity)
                .join(QBookmarkEntity.bookmarkEntity.record, QRecordEntity.recordEntity)
                .join(QBookmarkEntity.bookmarkEntity.user, QUserEntity.userEntity)
                .where(
                        QueryDslUtils.ltCursorId(cursor, QBookmarkEntity.bookmarkEntity.id),
                        QUserEntity.userEntity.eq(userEntity)

                )
                .orderBy(QBookmarkEntity.bookmarkEntity.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(bookmarkEntities, pageable, QueryDslUtils.hasNext(pageable, bookmarkEntities));
    }

}
