package org.recordy.server.subscribe.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.subscribe.domain.Subscribe;
import org.recordy.server.subscribe.domain.usecase.SubscribeCreate;
import org.recordy.server.subscribe.repository.SubscribeRepository;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.util.DomainFixture;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SubscribeServiceTest {

    private SubscribeService subscribeService;
    private SubscribeRepository subscribeRepository;
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        FakeContainer fakeContainer = new FakeContainer();
        subscribeService = fakeContainer.subscribeService;
        subscribeRepository = fakeContainer.subscribeRepository;
        userRepository = fakeContainer.userRepository;
    }

    @Test
    void subscribe를_통해_사용자_간_구독할_수_있고_결과로_true를_반환한다() {
        // given
        userRepository.save(DomainFixture.createUser(1));
        userRepository.save(DomainFixture.createUser(2));

        // when
        boolean result = subscribeService.subscribe(new SubscribeCreate(1, 2));
        Slice<Subscribe> subscribes = subscribeRepository.findAllBySubscribingUserId(1, 20, PageRequest.ofSize(3));

        // then
        assertAll(
                () -> assertThat(result).isTrue(),
                () -> assertThat(subscribes.getContent().size()).isEqualTo(1),
                () -> assertThat(subscribes.getContent().get(0).getSubscribedUser().getId()).isEqualTo(2)
        );
    }

    @Test
    void subscribe를_통해_사용자_간_구독을_해지할_수_있고_결과로_false를_반환한다() {
        // given
        userRepository.save(DomainFixture.createUser(1));
        userRepository.save(DomainFixture.createUser(2));

        subscribeService.subscribe(new SubscribeCreate(1, 2));

        // when
        boolean result = subscribeService.subscribe(new SubscribeCreate(1, 2));

        // then
        assertAll(
                () -> assertThat(result).isFalse(),
                () -> assertThat(subscribeRepository.existsBySubscribingUserIdAndSubscribedUserId(1, 2)).isFalse()
        );
    }

    @Test
    void getSubscribedUsers를_통해_구독한_사용자를_조회할_수_있다() {
        // given
        userRepository.save(DomainFixture.createUser(1));
        userRepository.save(DomainFixture.createUser(2));
        userRepository.save(DomainFixture.createUser(3));

        subscribeService.subscribe(new SubscribeCreate(1, 2));
        subscribeService.subscribe(new SubscribeCreate(1, 3));

        // when
        Slice<User> result = subscribeService.getSubscribedUsers(1, 20, 3);

        // then
        assertAll(
                () -> assertThat(result.getContent().size()).isEqualTo(2),
                () -> assertThat(result.getContent().get(0).getId()).isEqualTo(3),
                () -> assertThat(result.getContent().get(1).getId()).isEqualTo(2)
        );
    }
}