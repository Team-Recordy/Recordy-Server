package org.recordy.server.user.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserEntity;
import org.recordy.server.user.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        return userJpaRepository.save(UserEntity.from(user))
                .toDomain();
    }

    @Override
    public Optional<User> findByPlatformId(String platformId) {
        return userJpaRepository.findByPlatformId(platformId)
                .map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId)
                .map(UserEntity::toDomain);
    }
}
