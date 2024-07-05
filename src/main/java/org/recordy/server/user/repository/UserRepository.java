package org.recordy.server.user.repository;

import org.recordy.server.user.domain.User;

import java.util.Optional;

public interface UserRepository {

    // command
    User save(User user);

    void deleteById(long userId);

    // query
    Optional<User> findByPlatformId(String platformId);

    boolean existsByNickname(String nickname);

    Optional<User> findById(long userId);
}
