package org.recordy.server.user.controller.dto.response;

import java.util.stream.Collectors;

import org.recordy.server.common.dto.response.CursorResponse;
import org.recordy.server.user.domain.User;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public record UserInfo(
        Long id,
        String nickname,
        String profileImageUrl
) implements CursorResponse {

    public static UserInfo from(User user) {
        return new UserInfo(
                user.getId(),
                user.getNickname(),
                user.getProfileImageUrl()
        );
    }

    public static Slice<UserInfo> from(Slice<User> users) {
        return new SliceImpl<>(
                users.getContent().stream()
                        .map(UserInfo::from)
                        .collect(Collectors.toList()),
                users.getPageable(),
                users.hasNext()
        );
    }

    @Override
    public long getId() {
        return id;
    }
}
