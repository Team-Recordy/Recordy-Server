package org.recordy.server.keyword.repository.impl;

import org.recordy.server.keyword.domain.KeywordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordJpaRepository extends JpaRepository<KeywordEntity, Long> {
}
