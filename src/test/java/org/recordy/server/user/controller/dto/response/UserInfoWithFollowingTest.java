package org.recordy.server.user.controller.dto.response;

import org.junit.jupiter.api.Test;
import org.recordy.server.user.domain.User;
import org.recordy.server.util.DomainFixture;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserInfoWithFollowingTest {

    @Test
    void of를_통해_User와_Boolean_리스트를_합쳐서_UserInfoWithFollowing_Slice를_생성한다() {
        // given
        List<User> users = new ArrayList<>();
        List<Boolean> isFollowing = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            users.add(DomainFixture.createUser());
            isFollowing.add(true);
        }

        Pageable pageable = PageRequest.ofSize(10);
        boolean hasNext = true;
        SliceImpl<User> userSlice = new SliceImpl<>(users, pageable, hasNext);

        // when
        Slice<UserInfoWithFollowing> userInfoWithFollowings = UserInfoWithFollowing.of(userSlice, isFollowing);

        // then
        assertAll(
                () -> assertThat(userInfoWithFollowings.getContent().size()).isEqualTo(users.size()),
                () -> assertThat(userInfoWithFollowings.getContent().size()).isEqualTo(isFollowing.size()),
                () -> assertThat(userInfoWithFollowings.getPageable()).isEqualTo(userSlice.getPageable())
        );
    }
}