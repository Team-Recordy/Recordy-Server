package org.recordy.server.auth.repository.impl;

import org.recordy.server.auth.domain.AuthEntity;
import org.springframework.data.repository.CrudRepository;

public interface AuthRedisRepository extends CrudRepository<AuthEntity, String> {
}
