package org.recordy.server.auth.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthEntity;
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
}
