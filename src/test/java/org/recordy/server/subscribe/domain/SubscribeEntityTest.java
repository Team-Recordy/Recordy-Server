package org.recordy.server.subscribe.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.user.domain.UserEntity;
import org.recordy.server.util.DomainFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SubscribeEntityTest {

    @Test
    void from을_통해_구독_객체로부터_구독_엔티티를_생성할_수_있다() {
        // given
        Subscribe subscribe = Subscribe.builder()
                .id(1L)
                .subscribingUser(DomainFixture.createUser(1))
                .subscribedUser(DomainFixture.createUser(2))
                .build();

        // when
        SubscribeEntity subscribeEntity = SubscribeEntity.from(subscribe);

        // then
        assertAll(
                () -> assertThat(subscribe.getId()).isEqualTo(subscribeEntity.getId()),
                () -> assertThat(subscribe.getSubscribingUser().getId()).isEqualTo(subscribeEntity.getSubscribingUser().getId()),
                () -> assertThat(subscribe.getSubscribedUser().getId()).isEqualTo(subscribeEntity.getSubscribedUser().getId())
        );
    }

    @Test
    void toDomain을_통해_구독_엔티티로부터_구독_객체를_생성할_수_있다() {
        // given
        SubscribeEntity subscribeEntity = SubscribeEntity.builder()
                .subscribedUser(UserEntity.from(DomainFixture.createUser(1)))
                .subscribingUser(UserEntity.from(DomainFixture.createUser(2)))
                .build();

        // when
        Subscribe subscribe = subscribeEntity.toDomain();

        // then
        assertAll(
                () -> assertThat(subscribeEntity.getId()).isEqualTo(subscribe.getId()),
                () -> assertThat(subscribeEntity.getSubscribingUser().getId()).isEqualTo(subscribe.getSubscribingUser().getId()),
                () -> assertThat(subscribeEntity.getSubscribedUser().getId()).isEqualTo(subscribe.getSubscribedUser().getId())
        );
    }
}