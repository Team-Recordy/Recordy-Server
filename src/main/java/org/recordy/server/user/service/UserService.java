package org.recordy.server.user.service;

import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.user.domain.User;

import java.util.Optional;

public interface UserService {

    // command
    User create(AuthPlatform platform);

    // query
    Optional<User> getByPlatformId(String platformId);

    // validation
    void validateDuplicateNickname(String nickname);

}
