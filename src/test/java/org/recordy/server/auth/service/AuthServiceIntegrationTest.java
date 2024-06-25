package org.recordy.server.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.domain.usecase.AuthSignIn;
import org.recordy.server.auth.repository.AuthRepository;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.util.DomainFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class AuthServiceIntegrationTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private AuthPlatformServiceFactory authPlatformServiceFactory;

    @MockBean
    private AuthTokenService authTokenService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        // AuthServiceImpl이 AuthPlatformService의 getPlatform()을 호출하면, FakeAuthKakaoPlatformServiceImpl의 getPlatform()이 호출된다.
        // 즉, DomainFixture.PLATFORM_ID와 AuthPlatform.Type.KAKAO가 반환된다.
        Mockito.when(authPlatformServiceFactory.getPlatformServiceFrom(Mockito.any(AuthPlatform.Type.class)))
                .thenReturn(new FakeContainer().authKakaoPlatformService);

        // AuthServiceImpl이 AuthTokenService의 issueToken()을 호출하면, FakeAuthTokenServiceImpl의 issueToken()이 호출된다.
        // 즉, DomainFixture.createAuthToken()이 반환된다.
        Mockito.when(authTokenService.issueToken(Mockito.anyLong()))
                .thenReturn(DomainFixture.createAuthToken());
    }

    @Test
    void signIn을_요청한_가입되지_않은_사용자에_대해_새로운_User_객체가_저장된다() {
        // given
        AuthPlatform.Type platformType = DomainFixture.PLATFORM_TYPE;
        AuthSignIn authSignIn = DomainFixture.createAuthSignIn(platformType);


        // when
        Auth auth = authService.signIn(authSignIn);
        Optional<User> user = userRepository.findByPlatformId(auth.getPlatform().getId());

        // then
        assertAll(
                () -> assertThat(user).isNotEmpty(),
                () -> assertThat(user.get().getAuthPlatform().getId()).isEqualTo(auth.getPlatform().getId()),
                () -> assertThat(user.get().getAuthPlatform().getType()).isEqualTo(auth.getPlatform().getType())
        );
    }

    @Test
    void signIn을_요청한_가입되지_않은_사용자는_isSignedUp이_false인_Auth_객체를_반환한다() {
        // given
        AuthPlatform.Type platformType = DomainFixture.PLATFORM_TYPE;
        AuthSignIn authSignIn = DomainFixture.createAuthSignIn(platformType);

        // when
        Auth auth = authService.signIn(authSignIn);

        // then
        assertAll(
                () -> assertThat(auth.isSignedUp()).isFalse()
        );
    }

    @Test
    void signIn을_요청한_가입되지_않은_사용자는_PENDING_상태로_가입_처리된다() {
        // given
        AuthPlatform.Type platformType = DomainFixture.PLATFORM_TYPE;
        AuthSignIn authSignIn = DomainFixture.createAuthSignIn(platformType);

        // when
        Auth auth = authService.signIn(authSignIn);
        Optional<User> user = userRepository.findByPlatformId(auth.getPlatform().getId());

        // then
        assertAll(
                () -> assertThat(user).isNotEmpty(),
                () -> assertThat(user.get().getAuthPlatform().getId()).isEqualTo(auth.getPlatform().getId()),
                () -> assertThat(user.get().getAuthPlatform().getType()).isEqualTo(auth.getPlatform().getType()),
                () -> assertThat(user.get().getStatus()).isEqualTo(UserStatus.PENDING)
        );
    }

    @Test
    void signIn을_요청한_이미_가입된_사용자는_isSignedUp이_true인_Auth_객체를_반환한다() {
        // given
        AuthPlatform.Type platformType = DomainFixture.PLATFORM_TYPE;
        AuthSignIn authSignIn = DomainFixture.createAuthSignIn(platformType);

        userRepository.save(DomainFixture.createUser());

        // when
        Auth auth = authService.signIn(authSignIn);

        // then
        assertAll(
                () -> assertThat(auth.isSignedUp()).isTrue()
        );
    }

    @Test
    void signIn을_요청한_이미_가입된_사용자는_ACTIVE_상태다() {
        // given
        AuthPlatform.Type platformType = DomainFixture.PLATFORM_TYPE;
        AuthSignIn authSignIn = DomainFixture.createAuthSignIn(platformType);

        userRepository.save(DomainFixture.createUser());

        // when
        Auth auth = authService.signIn(authSignIn);
        Optional<User> user = userRepository.findByPlatformId(auth.getPlatform().getId());

        // then
        assertAll(
                () -> assertThat(user).isNotEmpty(),
                () -> assertThat(user.get().getStatus()).isEqualTo(UserStatus.ACTIVE)
        );
    }
}