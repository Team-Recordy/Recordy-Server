package org.recordy.server.user.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.util.DomainFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserEntityTest {

    @Test
    void from을_통해_User_객체로부터_UserEntity_객체를_생성할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .authPlatform(DomainFixture.createAuthPlatform())
                .status(UserStatus.ACTIVE)
                .build();

        // when
        UserEntity userEntity = UserEntity.from(user);

        // then
        assertAll(
                () -> assertThat(userEntity.getId()).isEqualTo(user.getId()),
                () -> assertThat(userEntity.getPlatformId()).isEqualTo(user.getAuthPlatform().getId()),
                () -> assertThat(userEntity.getPlatformType()).isEqualTo(user.getAuthPlatform().getType()),
                () -> assertThat(userEntity.getStatus()).isEqualTo(user.getStatus())
        );
    }

    @Test
    void from을_통해_넘어온_User_객체의_id_필드가_null일_수_있다() {
        // given
        User user = User.builder()
                .id(null)
                .authPlatform(DomainFixture.createAuthPlatform())
                .status(UserStatus.ACTIVE)
                .build();

        // when
        UserEntity userEntity = UserEntity.from(user);

        // then
        assertAll(
                () -> assertThat(userEntity.getId()).isEqualTo(null),
                () -> assertThat(userEntity.getPlatformId()).isEqualTo(user.getAuthPlatform().getId()),
                () -> assertThat(userEntity.getPlatformType()).isEqualTo(user.getAuthPlatform().getType()),
                () -> assertThat(userEntity.getStatus()).isEqualTo(user.getStatus())
        );
    }

    @Test
    void toDomain을_통해_UserEntity_객체로부터_User_객체를_생성할_수_있다() {
        // given
        UserEntity userEntity = DomainFixture.createUserEntity();

        // when
        User user = userEntity.toDomain();

        // then
        assertAll(
                () -> assertThat(user.getId()).isEqualTo(userEntity.getId()),
                () -> assertThat(user.getAuthPlatform().getId()).isEqualTo(userEntity.getPlatformId()),
                () -> assertThat(user.getAuthPlatform().getType()).isEqualTo(userEntity.getPlatformType()),
                () -> assertThat(user.getStatus()).isEqualTo(userEntity.getStatus())
        );
    }
}