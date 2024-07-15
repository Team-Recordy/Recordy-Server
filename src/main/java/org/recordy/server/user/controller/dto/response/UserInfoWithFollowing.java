package org.recordy.server.user.controller.dto.response;

import java.util.List;
import org.recordy.server.user.domain.User;
import org.springframework.data.domain.Slice;

public record UserInfoWithFollowing(
        UserInfo userInfo,
        Boolean following
) {
    public static Slice<UserInfoWithFollowing> of (Slice<User> users, List<Boolean> following) {
        return users.map(user -> {
            int index = users.getContent().indexOf(user);
            Boolean isFollowing = following.get(index);
            UserInfo userInfo = UserInfo.from(user);
            return new UserInfoWithFollowing(userInfo,isFollowing);
        });
    }
}
