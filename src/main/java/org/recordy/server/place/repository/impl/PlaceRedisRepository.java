package org.recordy.server.place.repository.impl;

import org.recordy.server.place.domain.PlaceEntity;
import org.springframework.data.repository.CrudRepository;

public interface PlaceRedisRepository extends CrudRepository<PlaceEntity, Long> {

    boolean existsByPlatformId(String platformId);
}
