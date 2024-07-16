package org.recordy.server.bookmark.repository.impl;

import org.recordy.server.bookmark.domain.BookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkJpaRepository extends JpaRepository<BookmarkEntity, Long> {
    // command
    void deleteAllByUserIdAndRecordId(long userId, long recordId);

    // query
    boolean existsByUserIdAndRecordId(Long userId, Long recordId);
}
