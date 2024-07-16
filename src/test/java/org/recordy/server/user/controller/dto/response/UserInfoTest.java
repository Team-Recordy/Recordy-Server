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

class UserInfoTest {

    @Test
    void from을_통해_User_객체로부터_UserInfo를_생성할_수_있다() {
        // given
        User user = DomainFixture.createUser();

        // when
        UserInfo result = UserInfo.from(user);

        // then
        assertAll(
                () -> assertThat(result.id()).isEqualTo(user.getId()),
                () -> assertThat(result.nickname()).isEqualTo(user.getNickname()),
                () -> assertThat(result.profileImageUrl()).isEqualTo(user.getProfileImageUrl())
        );
    }

    @Test
    void of를_통해_User의_Slice를_UserInfo의_Slice로_변환할_수_있다() {
        // given
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            users.add(DomainFixture.createUser());
        }
        Pageable pageable = PageRequest.ofSize(10);
        boolean hasNext = true;
        SliceImpl<User> userSlice = new SliceImpl<>(users, pageable, hasNext);

        // when
        Slice<UserInfo> userInfoSlice = UserInfo.from(userSlice);

        // then
        assertAll(
                () -> assertThat(userInfoSlice.getContent().size()).isEqualTo(userSlice.getContent().size()),
                () -> assertThat(userInfoSlice.getPageable()).isEqualTo(userSlice.getPageable()),
                () -> assertThat(userInfoSlice.hasNext()).isEqualTo(userSlice.hasNext())
        );
    }
}