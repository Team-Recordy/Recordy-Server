package org.recordy.server.user.controller.dto.response;

import org.recordy.server.common.dto.response.CursorResponse;

public record UserInfo(
        Long id,
        String nickname,
        String profileImageUrl,
        boolean isFollowing
) implements CursorResponse {

    @Override
    public long getId() {
        return id;
    }
}
