package org.recordy.server.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.AuthToken;
import org.recordy.server.auth.service.AuthTokenService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthTokenServiceImpl implements AuthTokenService {

    @Override
    public AuthToken issueToken(long userId) {
        return null;
    }
}