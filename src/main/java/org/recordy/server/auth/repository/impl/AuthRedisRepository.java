package org.recordy.server.auth.repository.impl;

import java.util.Optional;
import org.recordy.server.auth.domain.AuthEntity;
import org.springframework.data.repository.CrudRepository;

public interface AuthRedisRepository extends CrudRepository<AuthEntity, String> {

    Optional<AuthEntity> findByRefreshToken(String refreshToken);
}
