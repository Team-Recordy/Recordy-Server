package org.recordy.server.record.service.keyword.repository.impl;

import org.recordy.server.record.service.keyword.domain.KeywordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordJpaRepository extends JpaRepository<KeywordEntity, Long> {
}
