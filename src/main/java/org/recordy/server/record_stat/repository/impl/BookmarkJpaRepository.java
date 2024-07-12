package org.recordy.server.record_stat.repository.impl;

import java.util.Optional;
import org.recordy.server.record_stat.domain.BookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkJpaRepository extends JpaRepository<BookmarkEntity, Long> {

    // command
    void deleteAllByUserIdAndRecordId(long userId, long recordId);

    // query
    Optional<BookmarkEntity> findByUser_IdAndRecord_Id(long userId, long recordId);
}
