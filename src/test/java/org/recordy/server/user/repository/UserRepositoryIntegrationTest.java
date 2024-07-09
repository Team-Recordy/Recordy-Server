package org.recordy.server.user.repository;

import org.junit.jupiter.api.Test;
import org.recordy.server.user.controller.dto.request.TermsAgreement;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.util.DomainFixture;
import org.recordy.server.user.domain.User;
import org.recordy.server.util.db.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.recordy.server.util.DomainFixture.*;

@SqlGroup({
        @Sql(value = "/sql/user-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/user-repository-test-clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@SpringBootTest
class UserRepositoryIntegrationTest extends IntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save를_통해_유저_데이터를_저장할_수_있다() {
        // given
        User user = User.builder()
                .id(USER_ID + 1)
                .authPlatform(createAuthPlatform())
                .status(UserStatus.PENDING)
                .nickname(USER_NICKNAME)
                .termsAgreement(TermsAgreement.of(USE_TERM_AGREEMENT, PERSONAL_INFO_TERM_AGREEMENT, AGE_TERM_AGREEMENT))
                .build();

        // when
        User result = userRepository.save(user);

        // then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getAuthPlatform().getId()).isEqualTo(DomainFixture.PLATFORM_ID),
                () -> assertThat(result.getAuthPlatform().getType()).isEqualTo(DomainFixture.KAKAO_PLATFORM_TYPE),
                () -> assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING)
        );
    }

    @Test
    void deleteById를_통해_유저_데이터를_삭제할_수_있다() {
        // given
        User user = DomainFixture.createUser(UserStatus.ACTIVE);
        User savedUser = userRepository.save(user);

        // when
        userRepository.deleteById(savedUser.getId());

        // then
        assertThat(userRepository.findById(savedUser.getId())).isEmpty();
    }

    @Test
    void findByPlatformId를_통해_플랫폼_ID로_유저_데이터를_조회할_수_있다() {
        // when
        User result = userRepository.findByPlatformId(DomainFixture.PLATFORM_ID).orElse(null);

        // then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getAuthPlatform().getId()).isEqualTo(DomainFixture.PLATFORM_ID),
                () -> assertThat(result.getAuthPlatform().getType()).isEqualTo(DomainFixture.KAKAO_PLATFORM_TYPE),
                () -> assertThat(result.getStatus()).isEqualTo(DomainFixture.DEFAULT_USER_STATUS)
        );
    }

    @Test
    void findByPlatformId를_통해_존재하지_않는_플랫폼_ID로_유저_데이터를_조회하면_빈_값을_반환한다() {
        // when
        Optional<User> result = userRepository.findByPlatformId("non-exist-platform-id");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void existsByNickname를_통해_닉네임이_존재하는_경우_true를_반환한다() {
        // given
        userRepository.save(User.builder()
                .id(USER_ID + 1)
                .nickname(DomainFixture.USER_NICKNAME)
                .authPlatform(DomainFixture.createAuthPlatform())
                .termsAgreement(TermsAgreement.of(true, true, true))
                .build());

        // when
        boolean result = userRepository.existsByNickname(DomainFixture.USER_NICKNAME);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void existsByNickname를_통해_닉네임이_존재하지_않는_경우_false를_반환한다() {
        // when
        boolean result = userRepository.existsByNickname("non-exist-nickname");

        // then
        assertThat(result).isFalse();
    }

    @Test
    void findById를_통해_유저_ID로_유저_데이터를_조회할_수_있다() {
        // when
        User result = userRepository.findById(USER_ID).orElse(null);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(USER_ID),
                () -> assertThat(result.getAuthPlatform().getId()).isEqualTo(DomainFixture.PLATFORM_ID),
                () -> assertThat(result.getAuthPlatform().getType()).isEqualTo(DomainFixture.KAKAO_PLATFORM_TYPE),
                () -> assertThat(result.getStatus()).isEqualTo(DomainFixture.DEFAULT_USER_STATUS)
        );
    }

    @Test
    void findById를_통해_존재하지_않는_유저_ID로_유저_데이터를_조회하면_빈_값을_반환한다() {
        // when
        Optional<User> result = userRepository.findById(0);

        // then
        assertThat(result).isEmpty();
    }
}