package org.recordy.server.user.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.subscribe.domain.SubscribeEntity;
import org.recordy.server.subscribe.repository.impl.SubscribeJpaRepository;
import org.recordy.server.user.controller.dto.response.UserInfo;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserEntity;
import org.recordy.server.user.exception.UserException;
import org.recordy.server.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    @Value("${user.root.id}")
    private Long rootUserId;

    private final UserJpaRepository userJpaRepository;
    private final UserQueryDslRepository userQueryDslRepository;
    private final SubscribeJpaRepository subscribeJpaRepository;

    @Override
    public User save(User user) {
        UserEntity entity = userJpaRepository.save(UserEntity.from(user));
        followRootUser(entity);

        return User.from(entity);
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
    public void deleteById(long id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public User findById(long id) {
        UserEntity entity = userQueryDslRepository.findById(id);

        if (Objects.isNull(entity))
            throw new UserException(ErrorMessage.USER_NOT_FOUND);

        return User.from(entity);
    }

    @Override
    public User findByPlatformId(String platformId) {
        UserEntity entity = userQueryDslRepository.findByPlatformId(platformId);

        if (Objects.isNull(entity))
            throw new UserException(ErrorMessage.USER_NOT_FOUND);

        return User.from(entity);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return userJpaRepository.existsByNickname(nickname);
    }

    @Override
    public Slice<UserInfo> findFollowings(long userId, Long cursor, int size) {
        return userQueryDslRepository.findFollowings(userId, cursor, size);
    }

    @Override
    public Slice<UserInfo> findFollowers(long userId, Long cursor, int size) {
        return userQueryDslRepository.findFollowers(userId, cursor, size);
    }
}
