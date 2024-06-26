package org.recordy.server.auth.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.util.DomainFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AuthEntityTest {

    @Test
    void from을_통해_Auth_객체로부터_AuthEntity_객체를_생성할_수_있다() {
        // given
        Auth auth = Auth.builder()
                .platform(DomainFixture.createAuthPlatform())
                .token(DomainFixture.createAuthToken())
                .isSignedUp(true)
                .build();

        // when
        AuthEntity authEntity = AuthEntity.from(auth);

        // then
        assertAll(
                () -> assertThat(authEntity.getPlatformId()).isEqualTo(auth.getPlatform().getId()),
                () -> assertThat(authEntity.getPlatformType()).isEqualTo(auth.getPlatform().getType().name()),
                () -> assertThat(authEntity.getAccessToken()).isEqualTo(auth.getToken().getAccessToken()),
                () -> assertThat(authEntity.getRefreshToken()).isEqualTo(auth.getToken().getRefreshToken()),
                () -> assertThat(authEntity.isSignedUp()).isEqualTo(auth.isSignedUp())
        );
    }

    @Test
    void toDomain을_통해_AuthEntity_객체로부터_Auth_객체를_생성할_수_있다() {
        // given
        AuthEntity authEntity = DomainFixture.createAuthEntity(true);

        // when
        Auth auth = authEntity.toDomain();

        // then
        assertAll(
                () -> assertThat(auth.getPlatform().getId()).isEqualTo(authEntity.getPlatformId()),
                () -> assertThat(auth.getPlatform().getType().name()).isEqualTo(authEntity.getPlatformType()),
                () -> assertThat(auth.getToken().getAccessToken()).isEqualTo(authEntity.getAccessToken()),
                () -> assertThat(auth.getToken().getRefreshToken()).isEqualTo(authEntity.getRefreshToken()),
                () -> assertThat(auth.isSignedUp()).isEqualTo(authEntity.isSignedUp())
        );
    }
}