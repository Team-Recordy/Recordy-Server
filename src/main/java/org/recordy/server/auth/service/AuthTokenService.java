package org.recordy.server.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import org.recordy.server.auth.domain.AuthToken;
import org.recordy.server.auth.service.dto.AuthTokenValidationResult;

public interface AuthTokenService {

    AuthToken issueToken(long userId);
    AuthTokenValidationResult validateToken(String token);
    String getPlatfromIdFromRefreshToken(String refreshToken);
    String getTokenFromRequest(HttpServletRequest request);
    long getUserIdFromToken(String token);
    String issueAccessToken(long userId);
}
