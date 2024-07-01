package org.recordy.server.user.repository;

import org.recordy.server.user.domain.User;

import java.util.Optional;

public interface UserRepository {

    // command
    User save(User user);

    // query
    Optional<User> findByPlatformId(String platformId);
    Optional<User> findById(Long userId);
}
