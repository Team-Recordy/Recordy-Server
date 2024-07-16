package org.recordy.server.view.repository.impl;

import org.recordy.server.view.domain.ViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewJpaRepository extends JpaRepository<ViewEntity, Long> {
}
