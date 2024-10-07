package org.recordy.server.place.repository.impl;

import org.recordy.server.place.domain.PlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceJpaRepository extends JpaRepository<PlaceEntity, Long> {
}
