package org.recordy.server.bookmark.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.bookmark.domain.Bookmark;
import org.recordy.server.bookmark.domain.BookmarkEntity;
import org.recordy.server.bookmark.repository.BookmarkRepository;
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
    public void deleteByUserId(long userId) {
        bookmarkJpaRepository.deleteAllByUserId(userId);
    }

    @Override
    public Slice<Bookmark> findAllByBookmarksOrderByIdDesc(long userId, long cursor, Pageable pageable) {
        return bookmarkQueryDslRepository.findAllByUserOrderByIdDesc(userId, cursor, pageable)
                .map(BookmarkEntity::toDomain);
    }

    @Override
    public boolean existsByUserIdAndRecordId(Long userId, Long recordId) {
        return bookmarkJpaRepository.existsByUserIdAndRecordId(userId, recordId);
    }

    @Override
    public long countByUserId(Long userId) {
        return countByUserId(userId);
    }
}
