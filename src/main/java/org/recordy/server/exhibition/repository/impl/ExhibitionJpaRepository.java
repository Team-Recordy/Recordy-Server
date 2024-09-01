package org.recordy.server.exhibition.repository.impl;

import org.recordy.server.exhibition.domain.ExhibitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExhibitionJpaRepository extends JpaRepository<ExhibitionEntity, Long> {
}
