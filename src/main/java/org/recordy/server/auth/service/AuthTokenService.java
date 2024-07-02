package org.recordy.server.auth.service;

import org.recordy.server.auth.domain.AuthToken;
import org.recordy.server.auth.service.dto.AuthTokenValidationResult;

public interface AuthTokenService {

    AuthToken issueToken(long userId);
    AuthTokenValidationResult validateToken(String token);
    String reissueToken(String refreshToken);
}
