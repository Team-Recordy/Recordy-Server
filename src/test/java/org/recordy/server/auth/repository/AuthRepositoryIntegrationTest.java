package org.recordy.server.auth.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.util.DomainFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class AuthRepositoryIntegrationTest {

    @Autowired
    private AuthRepository authRepository;

    @Test
    void save를_통해_인증_데이터를_저장할_수_있다() {
        // given
        String id = "abc";
        boolean isSignedUp = true;

        Auth auth = new Auth(
                new AuthPlatform(id, AuthPlatform.Type.KAKAO),
                DomainFixture.createAuthToken(),
                isSignedUp
        );

        // when
        Auth result = authRepository.save(auth);

        // then
        assertAll(
                () -> assertThat(result.getPlatform().getId()).isEqualTo(id),
                () -> assertThat(result.getPlatform().getType()).isEqualTo(AuthPlatform.Type.KAKAO),
                () -> assertThat(result.getToken().getAccessToken()).isEqualTo(DomainFixture.ACCESS_TOKEN),
                () -> assertThat(result.getToken().getRefreshToken()).isEqualTo(DomainFixture.REFRESH_TOKEN),
                () -> assertThat(result.isSignedUp()).isEqualTo(isSignedUp)
        );
    }
}
