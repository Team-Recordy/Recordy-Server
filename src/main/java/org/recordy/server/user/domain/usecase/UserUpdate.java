package org.recordy.server.user.domain.usecase;

import org.recordy.server.user.controller.dto.request.UserUpdateRequest;

public record UserUpdate(
        String nickname,
        String profileImageUrl
) {

    public static UserUpdate from(UserUpdateRequest request) {
        return new UserUpdate(
                request.nickname(),
                request.profileImageUrl()
        );
    }
}
