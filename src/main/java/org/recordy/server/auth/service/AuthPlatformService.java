package org.recordy.server.auth.service;

import org.recordy.server.auth.domain.usecase.AuthSignIn;
import org.recordy.server.auth.domain.AuthPlatform;

public interface AuthPlatformService {

    AuthPlatform getPlatform(AuthSignIn authSignIn);
    AuthPlatform.Type getPlatformType();
}
