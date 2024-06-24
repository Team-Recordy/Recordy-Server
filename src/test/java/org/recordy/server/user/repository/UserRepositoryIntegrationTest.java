package org.recordy.server.user.repository;

import org.junit.jupiter.api.Test;
import org.recordy.server.util.DomainFixture;
import org.recordy.server.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SqlGroup({
        @Sql(value = "/sql/user-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/user-repository-test-clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@SpringBootTest
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save를_통해_유저_데이터를_저장할_수_있다() {
        // given
        User user = DomainFixture.createUser();

        // when
        User result = userRepository.save(user);

        // then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getAuthPlatform().getId()).isEqualTo(DomainFixture.PLATFORM_ID),
                () -> assertThat(result.getAuthPlatform().getType()).isEqualTo(DomainFixture.PLATFORM_TYPE),
                () -> assertThat(result.getStatus()).isEqualTo(DomainFixture.DEFAULT_USER_STATUS)
        );
    }

    @Test
    void findByPlatformId를_통해_플랫폼_ID로_유저_데이터를_조회할_수_있다() {
        // when
        User result = userRepository.findByPlatformId(DomainFixture.PLATFORM_ID).orElse(null);

        // then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getAuthPlatform().getId()).isEqualTo(DomainFixture.PLATFORM_ID),
                () -> assertThat(result.getAuthPlatform().getType()).isEqualTo(DomainFixture.PLATFORM_TYPE),
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
}