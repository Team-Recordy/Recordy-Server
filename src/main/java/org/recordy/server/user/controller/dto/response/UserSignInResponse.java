package org.recordy.server.user.controller.dto.response;

import org.recordy.server.auth.domain.Auth;

public record UserSignInResponse(
        String accessToken,
        String refreshToken,
        boolean isSignedUp
) {

    public static UserSignInResponse from(Auth auth) {
        return new UserSignInResponse(
                auth.getToken().getAccessToken(),
                auth.getToken().getRefreshToken(),
                auth.isSignedUp()
        );
    }
}
