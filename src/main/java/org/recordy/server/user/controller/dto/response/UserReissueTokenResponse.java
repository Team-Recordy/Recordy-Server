package org.recordy.server.user.controller.dto.response;

public record UserReissueTokenResponse(
        String accessToken
) {
    public static UserReissueTokenResponse of (String accessToken) {
        return new UserReissueTokenResponse(accessToken);
    }
}
