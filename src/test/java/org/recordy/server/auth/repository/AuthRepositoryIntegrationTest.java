package org.recordy.server.auth.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.util.DomainFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
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

    @Test
    void findByPlatformId를_통해_플랫폼_ID로_인증_데이터를_조회할_수_있다() {
        // given
        String id = "abc";
        boolean isSignedUp = true;

        Auth auth = new Auth(
                new AuthPlatform(id, AuthPlatform.Type.KAKAO),
                DomainFixture.createAuthToken(),
                isSignedUp
        );
        authRepository.save(auth);

        // when
        Auth result = authRepository.findByPlatformId(id).orElse(null);

        // then
        assertAll(
                () -> assertThat(result.getPlatform().getId()).isEqualTo(id),
                () -> assertThat(result.getPlatform().getType()).isEqualTo(AuthPlatform.Type.KAKAO),
                () -> assertThat(result.getToken().getAccessToken()).isEqualTo(DomainFixture.ACCESS_TOKEN),
                () -> assertThat(result.getToken().getRefreshToken()).isEqualTo(DomainFixture.REFRESH_TOKEN),
                () -> assertThat(result.isSignedUp()).isEqualTo(isSignedUp)
        );
    }

    @Test
    void findByPlatformId를_통해_존재하지_않는_플랫폼_ID로_인증_데이터를_조회하면_빈_값을_반환한다() {
        // when
        Optional<Auth> result = authRepository.findByPlatformId("non-exist-platform-id");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void delete를_통해_Auth_객체를_삭제할_수_있다() {
        // given
        String id = "abc";
        boolean isSignedUp = true;

        Auth auth = new Auth(
                new AuthPlatform(id, AuthPlatform.Type.KAKAO),
                DomainFixture.createAuthToken(),
                isSignedUp
        );
        authRepository.save(auth);

        // when
        authRepository.delete(auth);

        // then
        assertThat(authRepository.findByPlatformId(id)).isEmpty();
    }

    @Test
    void delete를_통해_존재하지_않는_Auth_객체를_삭제하더라도_에러가_발생하지_않는다() {
        // given
        String id = "abc";
        boolean isSignedUp = true;

        Auth auth = new Auth(
                new AuthPlatform(id, AuthPlatform.Type.KAKAO),
                DomainFixture.createAuthToken(),
                isSignedUp
        );

        // when, then
        assertThatCode(() -> authRepository.delete(auth))
                .doesNotThrowAnyException();
    }
}
