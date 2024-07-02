package org.recordy.server.user.domain.usecase;

import org.recordy.server.auth.domain.AuthPlatform;

public record UserSignIn(
        String platformToken,
        AuthPlatform.Type platformType
) {

    public static UserSignIn of(String platformToken, AuthPlatform.Type platformType) {
        return new UserSignIn(platformToken, platformType);
    }
}
