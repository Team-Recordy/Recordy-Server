package org.recordy.server.bookmark.repository.impl;

import org.recordy.server.bookmark.domain.BookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface BookmarkJpaRepository extends JpaRepository<BookmarkEntity, Long> {
    // command
    void deleteAllByUserIdAndRecordId(long userId, long recordId);
    @Transactional
    void deleteAllByUserId(long userId);

    // query
    boolean existsByUserIdAndRecordId(Long userId, Long recordId);
    long countAllByUserId(Long userId);
}
