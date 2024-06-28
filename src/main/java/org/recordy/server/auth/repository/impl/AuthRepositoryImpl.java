package org.recordy.server.auth.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthEntity;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.auth.message.ErrorMessage;
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
    public Auth findByRefeshToken(String refreshToken) {
        return authRedisRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthException(ErrorMessage.AUTH_NOT_FOUND))
                .toDomain();
    }
}
