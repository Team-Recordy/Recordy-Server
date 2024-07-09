package org.recordy.server.record_stat.repository.impl;

import org.recordy.server.record_stat.domain.ViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewJpaRepository extends JpaRepository<ViewEntity, Long> {
}
