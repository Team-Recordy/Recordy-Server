package org.recordy.server.subscribe.repository;

import org.junit.jupiter.api.Test;
import org.recordy.server.subscribe.domain.Subscribe;
import org.recordy.server.subscribe.domain.SubscribeEntity;
import org.recordy.server.subscribe.repository.impl.SubscribeJpaRepository;
import org.recordy.server.util.DomainFixture;
import org.recordy.server.util.db.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SqlGroup({
        @Sql(value = "/sql/clean-database.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(value = "/sql/subscribe-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/clean-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class SubscribeRepositoryIntegrationTest extends IntegrationTest {

    @Autowired
    private SubscribeRepository subscribeRepository;

    @Autowired
    private SubscribeJpaRepository subscribeJpaRepository;

    @Test
    void save를_통해_구독_데이터를_생성할_수_있다() {
        // given
        Subscribe subscribe = Subscribe.builder()
                .subscribingUser(DomainFixture.createUser(1))
                .subscribedUser(DomainFixture.createUser(2))
                .build();
        subscribeRepository.save(subscribe);

        // when
        SubscribeEntity subscribeEntity = subscribeJpaRepository.findById(1L).get();

        // then
        assertAll(
                () -> assertThat(subscribeEntity.getId()).isEqualTo(1L),
                () -> assertThat(subscribeEntity.getSubscribingUser().getId()).isEqualTo(subscribe.getSubscribingUser().getId()),
                () -> assertThat(subscribeEntity.getSubscribedUser().getId()).isEqualTo(subscribe.getSubscribedUser().getId())
        );
    }

    @Test
    void delete를_통해_구독_데이터를_삭제할_수_있다() {
        // given
        Subscribe subscribe = Subscribe.builder()
                .subscribingUser(DomainFixture.createUser(1))
                .subscribedUser(DomainFixture.createUser(2))
                .build();
        subscribeRepository.save(subscribe);

        // when
        subscribeRepository.delete(1, 2);
        Optional<SubscribeEntity> subscribeEntity = subscribeJpaRepository.findById(1L);

        // then
        assertThat(subscribeEntity).isEmpty();
    }

    @Test
    void countSubscribingUsers를_통해_구독당하는_사용자의_구독자_수를_조회할_수_있다() {
        // given
        Subscribe subscribe1 = Subscribe.builder()
                .subscribingUser(DomainFixture.createUser(1))
                .subscribedUser(DomainFixture.createUser(2))
                .build();
        Subscribe subscribe2 = Subscribe.builder()
                .subscribingUser(DomainFixture.createUser(3))
                .subscribedUser(DomainFixture.createUser(2))
                .build();
        subscribeRepository.save(subscribe1);
        subscribeRepository.save(subscribe2);

        // when
        long result = subscribeRepository.countSubscribingUsers(2);

        // then
        assertThat(result).isEqualTo(2);
    }

    @Test
    void countSubscribedUsers를_통해_구독중인_사용자의_수를_조회할_수_있다() {
        // given
        Subscribe subscribe1 = Subscribe.builder()
                .subscribingUser(DomainFixture.createUser(1))
                .subscribedUser(DomainFixture.createUser(2))
                .build();
        Subscribe subscribe2 = Subscribe.builder()
                .subscribingUser(DomainFixture.createUser(1))
                .subscribedUser(DomainFixture.createUser(3))
                .build();
        subscribeRepository.save(subscribe1);
        subscribeRepository.save(subscribe2);

        // when
        long result = subscribeRepository.countSubscribedUsers(1);

        // then
        assertThat(result).isEqualTo(2);
    }

    @Test
    void existsBySubscribingUserIdAndSubscribedUserId를_통해_구독중인_사용자id와_구독당하는_사용자id로_구독_정보를_조회할_수_있다() {
        // given
        Subscribe subscribe = Subscribe.builder()
                .subscribingUser(DomainFixture.createUser(1))
                .subscribedUser(DomainFixture.createUser(2))
                .build();
        subscribeRepository.save(subscribe);

        // when
        boolean result = subscribeRepository.existsBySubscribingUserIdAndSubscribedUserId(1, 2);

        // then
        assertThat(result).isTrue();
    }
}