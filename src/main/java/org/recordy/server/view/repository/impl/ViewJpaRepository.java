package org.recordy.server.view.repository.impl;

import org.recordy.server.view.domain.ViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ViewJpaRepository extends JpaRepository<ViewEntity, Long> {

    @Transactional
    void deleteAllByUserId(long userId);
}
