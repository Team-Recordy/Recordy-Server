package org.recordy.server.record.repository.impl;

import org.recordy.server.record.domain.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordJpaRepository extends JpaRepository<RecordEntity, Long> {
}
