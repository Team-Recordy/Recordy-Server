package org.recordy.server.record_stat.repository.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.recordy.server.record_stat.domain.Bookmark;
import org.recordy.server.record_stat.domain.BookmarkEntity;
import org.recordy.server.record_stat.repository.BookmarkRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class BookmarkRepositoryImpl implements BookmarkRepository {

    private final BookmarkJpaRepository bookmarkJpaRepository;
    private final BookmarkQueryDslRepository bookmarkQueryDslRepository;

    @Override
    public Bookmark save(Bookmark bookmark) {
        return bookmarkJpaRepository.save(BookmarkEntity.from(bookmark))
                .toDomain();
    }

    @Transactional
    @Override
    public void delete(long userId, long recordId) {
        bookmarkJpaRepository.deleteAllByUserIdAndRecordId(userId, recordId);
    }

    @Override
    public Slice<Bookmark> findAllByBookmarksOrderByIdDesc(long userId, long cursor, Pageable pageable) {
        return bookmarkQueryDslRepository.findAllByUserOrderByIdDesc(userId, cursor, pageable)
                .map(BookmarkEntity::toDomain);
    }

    @Override
    public Optional<Bookmark> findByUserIdAndRecordId(long userId, long recordId) {
        return bookmarkJpaRepository.findByUser_IdAndRecord_Id(userId, recordId)
                .map(BookmarkEntity::toDomain);
    }
}
