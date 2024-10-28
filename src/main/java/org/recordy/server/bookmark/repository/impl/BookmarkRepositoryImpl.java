package org.recordy.server.bookmark.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.bookmark.domain.Bookmark;
import org.recordy.server.bookmark.domain.BookmarkEntity;
import org.recordy.server.bookmark.exception.BookmarkException;
import org.recordy.server.bookmark.repository.BookmarkRepository;
import org.recordy.server.common.message.ErrorMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class BookmarkRepositoryImpl implements BookmarkRepository {

    private final BookmarkJpaRepository bookmarkJpaRepository;
    private final BookmarkQueryDslRepository bookmarkQueryDslRepository;

    @Override
    public void save(Bookmark bookmark) {
        bookmarkJpaRepository.save(BookmarkEntity.from(bookmark));
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
    public Bookmark findById(Long id) {
        BookmarkEntity entity = bookmarkQueryDslRepository.findById(id);

        if (Objects.isNull(entity)) {
            throw new BookmarkException(ErrorMessage.BOOKMARK_NOT_FOUND);
        }

        return Bookmark.builder()
                .id(entity.getId())
                .build();
    }

    @Override
    public Slice<Bookmark> findAllByBookmarksOrderByIdDesc(long userId, Long cursor, Pageable pageable) {
        return bookmarkQueryDslRepository.findAllByUserOrderByIdDesc(userId, cursor, pageable)
                .map(BookmarkEntity::toDomain);
    }

    @Override
    public boolean existsByUserIdAndRecordId(Long userId, Long recordId) {
        return bookmarkJpaRepository.existsByUserIdAndRecordId(userId, recordId);
    }

    @Override
    public long countByUserId(Long userId) {
        return bookmarkJpaRepository.countAllByUserId(userId);
    }
}
