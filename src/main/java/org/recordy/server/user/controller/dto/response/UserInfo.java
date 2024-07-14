package org.recordy.server.user.controller.dto.response;

import java.util.stream.Collectors;
import org.recordy.server.user.domain.User;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public record UserInfo (
        Long id,
        String nickname
        //todo
        // 유저 프로필 추가
){
    public static UserInfo from(User user) {
        return new UserInfo(
                user.getId(),
                user.getNickname()
        );
    }

    public static Slice<UserInfo> of (Slice<User> users) {
        return new SliceImpl<>(
                users.getContent().stream()
                        .map(UserInfo::from)
                        .collect(Collectors.toList()),
                users.getPageable(),
                users.hasNext()
        );
    }
}
