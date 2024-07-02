package org.recordy.server.auth.service;

import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.auth.domain.AuthPlatform;

public interface AuthPlatformService {

    AuthPlatform getPlatform(UserSignIn userSignIn);
    AuthPlatform.Type getPlatformType();
}
