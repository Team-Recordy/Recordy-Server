package org.recordy.server.record_stat.repository.impl;

import org.recordy.server.record_stat.domain.BookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkJpaRepository extends JpaRepository<BookmarkEntity, Long> {

    Long countAllByRecord_Id(long recordId);
}
