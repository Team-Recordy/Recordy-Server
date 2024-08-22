package org.recordy.server.user.repository;

import org.recordy.server.user.domain.User;

import java.util.Optional;

public interface UserRepository {

    // command
    User save(User user);
    void deleteById(long userId);

    // query
    User findById(long userId);
    User findByPlatformId(String platformId);
    boolean existsByNickname(String nickname);
}
