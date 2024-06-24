package org.recordy.server.auth.domain.usecase;

import org.recordy.server.auth.domain.AuthPlatform;

public record AuthSignIn(
        String platformToken,
        AuthPlatform.Type platformType
) {

    public static AuthSignIn of(String platformToken, AuthPlatform.Type platformType) {
        return new AuthSignIn(platformToken, platformType);
    }
}
