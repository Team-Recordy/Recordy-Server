package org.recordy.server.auth.service;

import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.usecase.UserSignIn;

public interface AuthService {

    // command
    void deleteByPlatformId(String platformId);
    Auth create(User user, AuthPlatform platform);

    // query
    AuthPlatform getPlatform(UserSignIn userSignIn);
}
