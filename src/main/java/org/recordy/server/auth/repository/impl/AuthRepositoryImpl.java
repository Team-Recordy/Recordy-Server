package org.recordy.server.auth.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthEntity;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.auth.repository.AuthRepository;
import org.springframework.stereotype.Repository;

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
    public Auth findByRefeshToken(String refreshToken) {
        return authRedisRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthException(ErrorMessage.AUTH_NOT_FOUND))
                .toDomain();
    }

    @Override
    public Auth findByPlatformId(String platformId) {
        return  authRedisRepository.findByPlatformId(platformId)
                .orElseThrow(() -> new AuthException((ErrorMessage.AUTH_NOT_FOUND)))
                .toDomain();
    }
}
