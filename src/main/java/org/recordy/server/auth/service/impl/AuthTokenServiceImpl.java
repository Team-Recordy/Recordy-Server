package org.recordy.server.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.AuthToken;
import org.recordy.server.auth.service.AuthTokenService;
import org.recordy.server.auth.service.dto.AuthTokenValidationResult;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthTokenServiceImpl implements AuthTokenService {

    @Override
    public AuthToken issueToken(long userId) {
        return null;
    } // JWT 발급 로직

    @Override
    public AuthTokenValidationResult validateToken(String token) {
        return null;
    } // 토큰 검증 로직
}
