package org.recordy.server.user.service;

import org.recordy.server.user.domain.User;

import java.util.Optional;

public interface UserService {

    // command
    User create(User user);

    // query
    Optional<User> getByPlatformId(String platformId);
}
