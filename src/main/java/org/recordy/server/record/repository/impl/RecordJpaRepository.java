package org.recordy.server.record.repository.impl;

import org.recordy.server.record.domain.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RecordJpaRepository extends JpaRepository<RecordEntity, Long> {

    @Transactional
    void deleteAllByUserId(long userId);
}
