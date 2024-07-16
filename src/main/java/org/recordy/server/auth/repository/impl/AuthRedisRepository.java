package org.recordy.server.auth.repository.impl;

import org.recordy.server.auth.domain.AuthEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthRedisRepository extends CrudRepository<AuthEntity, String> {

    Optional<AuthEntity> findByRefreshToken(String refreshToken);
    Optional<AuthEntity> findByPlatformId(String platformId);
}
