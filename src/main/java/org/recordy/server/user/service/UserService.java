package org.recordy.server.user.service;

import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserStatus;

import java.util.Optional;

public interface UserService {

    // command
    User create(AuthPlatform platform, UserStatus userStatus);

    // query
    Optional<User> getByPlatformId(String platformId);
}
