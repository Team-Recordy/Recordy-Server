package org.recordy.server.user.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.subscribe.domain.SubscribeEntity;
import org.recordy.server.subscribe.repository.impl.SubscribeJpaRepository;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserEntity;
import org.recordy.server.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    @Value("${user.root.id}")
    private Long rootUserId;

    private final UserJpaRepository userJpaRepository;
    private final SubscribeJpaRepository subscribeJpaRepository;

    @Override
    public User save(User user) {
        UserEntity userEntity = userJpaRepository.save(UserEntity.from(user));
        followRootUser(userEntity);

        return userEntity.toDomain();
    }

    private void followRootUser(UserEntity userEntity) {
        if (!userEntity.getId().equals(rootUserId)) {
            userJpaRepository.findById(rootUserId)
                    .ifPresent(rootUser -> subscribeJpaRepository.save(SubscribeEntity.builder()
                            .subscribingUser(userEntity)
                            .subscribedUser(rootUser)
                            .build())
                    );
        }
    }

    @Override
    public void deleteById(long userId) {
        userJpaRepository.deleteById(userId);
    }

    @Override
    public Optional<User> findById(long userId) {
        return userJpaRepository.findById(userId)
                .map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByPlatformId(String platformId) {
        return userJpaRepository.findByPlatformId(platformId)
                .map(UserEntity::toDomain);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return userJpaRepository.existsByNickname(nickname);
    }
}
