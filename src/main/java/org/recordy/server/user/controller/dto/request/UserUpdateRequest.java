package org.recordy.server.user.controller.dto.request;

public record UserUpdateRequest(
        String nickname,
        String profileImageUrl
) {
}
