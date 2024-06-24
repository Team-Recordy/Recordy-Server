package org.recordy.server.auth.service;

import org.recordy.server.auth.domain.AuthToken;

public interface AuthTokenService {

    AuthToken issueToken(long userId);
}
