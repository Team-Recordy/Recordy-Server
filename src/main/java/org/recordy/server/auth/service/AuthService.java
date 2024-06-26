package org.recordy.server.auth.service;

import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.usecase.AuthSignIn;

public interface AuthService {

    Auth signIn(AuthSignIn authSignIn);
}
