package org.recordy.server.place.repository.impl;

import org.recordy.server.place.domain.PlaceCacheEntity;
import org.springframework.data.repository.CrudRepository;

public interface PlaceRedisRepository extends CrudRepository<PlaceCacheEntity, Long> {

    boolean existsByPlatformId(String platformId);
}
