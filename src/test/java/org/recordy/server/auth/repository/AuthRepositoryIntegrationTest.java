package org.recordy.server.auth.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.repository.impl.AuthRedisRepository;
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
    @Autowired
    private AuthRedisRepository authRedisRepository;

    @BeforeEach
    void tearDown() {
        authRedisRepository.deleteAll();
        authRedisRepository.findAll().forEach(System.out::println);
    }

    @Test
    void save를_통해_Auth_객체를_저장할_수_있다() {
        // given
        boolean isSignedUp = true;

        // when
        Auth result = authRepository.save(DomainFixture.createAuth(isSignedUp));

        // then
        assertAll(
                () -> assertThat(result.getPlatform().getId()).isEqualTo(DomainFixture.PLATFORM_ID),
                () -> assertThat(result.getPlatform().getType()).isEqualTo(AuthPlatform.Type.KAKAO),
                () -> assertThat(result.getToken().getAccessToken()).isEqualTo(DomainFixture.ACCESS_TOKEN),
                () -> assertThat(result.getToken().getRefreshToken()).isEqualTo(DomainFixture.REFRESH_TOKEN),
                () -> assertThat(result.isSignedUp()).isEqualTo(isSignedUp)
        );
    }

    @Test
    void delete를_통해_Auth_객체를_삭제할_수_있다() {
        // given
        Auth auth = authRepository.save(DomainFixture.createAuth(false));

        // when
        authRepository.delete(auth);

        // then
        assertThat(authRepository.findByPlatformId(auth.getPlatform().getId())).isEmpty();
    }

    @Test
    void delete를_통해_존재하지_않는_Auth_객체를_삭제하더라도_에러가_발생하지_않는다() {
        // given
        Auth auth = DomainFixture.createAuth(false);

        // when, then
        assertThatCode(() -> authRepository.delete(auth))
                .doesNotThrowAnyException();
    }

    @Test
    void findByPlatformId를_통해_플랫폼_ID로_인증_데이터를_조회할_수_있다() {
        // given
        boolean isSignedUp = true;
        authRepository.save(DomainFixture.createAuth(isSignedUp));

        // when
        Auth result = authRepository.findByPlatformId(DomainFixture.PLATFORM_ID)
                .orElse(null);

        // then
        assertAll(
                () -> assertThat(result.getPlatform().getId()).isEqualTo(DomainFixture.PLATFORM_ID),
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
    void findByRefreshToken을_통해_refresh_token으로_인증_데이터를_조회할_수_있다() {
        // given
        Auth auth = authRepository.save(DomainFixture.createAuth(true));

        // when
        Auth result = authRepository.findByRefreshToken(auth.getToken().getRefreshToken())
                .orElse(null);

        // then
        assertAll(
                () -> assertThat(result.getPlatform().getId()).isEqualTo(auth.getPlatform().getId()),
                () -> assertThat(result.getPlatform().getType()).isEqualTo(AuthPlatform.Type.KAKAO),
                () -> assertThat(result.getToken().getAccessToken()).isEqualTo(DomainFixture.ACCESS_TOKEN),
                () -> assertThat(result.getToken().getRefreshToken()).isEqualTo(DomainFixture.REFRESH_TOKEN),
                () -> assertThat(result.isSignedUp()).isEqualTo(auth.isSignedUp())
        );
    }

    @Test
    void findByRefreshToken을_통해_존재하지_않는_refresh_token으로_인증_데이터를_조회하면_빈_값을_반환한다() {
        //when
        Optional<Auth> result = authRepository.findByRefreshToken("non-exist-refresh-token");

        //then
        assertThat(result).isEmpty();
    }
}
