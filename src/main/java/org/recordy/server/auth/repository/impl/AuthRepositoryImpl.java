package org.recordy.server.auth.repository.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthEntity;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.auth.repository.AuthRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AuthRepositoryImpl implements AuthRepository {

    private final AuthRedisRepository authRedisRepository;

    @Override
    public Auth save(Auth auth) {
        return authRedisRepository.save(AuthEntity.from(auth))
                .toDomain();
    }

    @Override
    public void delete(Auth auth) {
        authRedisRepository.delete(AuthEntity.from(auth));
    }

    @Override
    public Optional<Auth> findByRefreshToken(String refreshToken) {
        return authRedisRepository.findByRefreshToken(refreshToken)
                .map(AuthEntity::toDomain);
    }

    @Override
    public Optional<Auth> findByPlatformId(String platformId) {
        return  authRedisRepository.findByPlatformId(platformId)
                .map(AuthEntity::toDomain);
    }
}
