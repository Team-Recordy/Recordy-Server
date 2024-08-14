package org.recordy.server.subscribe.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.recordy.server.subscribe.domain.Subscribe;
import org.recordy.server.subscribe.domain.usecase.SubscribeCreate;
import org.recordy.server.subscribe.repository.SubscribeRepository;
import org.recordy.server.subscribe.service.SubscribeService;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SubscribeServiceImpl implements SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public boolean subscribe(SubscribeCreate subscribeCreate) {
        if (subscribeRepository.existsBySubscribingUserIdAndSubscribedUserId(subscribeCreate.subscribingUserId(), subscribeCreate.subscribedUserId())) {
            subscribeRepository.delete(subscribeCreate.subscribingUserId(), subscribeCreate.subscribedUserId());
            return false;
        }

        User subscribingUser = userRepository.findById(subscribeCreate.subscribingUserId());
        User subscribedUser = userRepository.findById(subscribeCreate.subscribedUserId());

        subscribeRepository.save(Subscribe.builder()
                .subscribingUser(subscribingUser)
                .subscribedUser(subscribedUser)
                .build());

        return true;
    }

    @Override
    public Slice<User> getSubscribedUsers(long subscribingUserId, Long cursor, int size) {
        return subscribeRepository.findAllBySubscribingUserId(subscribingUserId, cursor, PageRequest.ofSize(size))
                .map(Subscribe::getSubscribedUser);
    }

    @Override
    public Slice<User> getSubscribingUsers(long subscribedUserId, Long cursor, int size) {
        return subscribeRepository.findAllBySubscribedUserId(subscribedUserId, cursor, PageRequest.ofSize(size))
                .map(Subscribe::getSubscribingUser);
    }

    @Override
    public List<Boolean> findSubscribes(long userId, Slice<User> subscribingUsers) {
        return subscribingUsers.getContent().stream()
                .map(subscribingUser -> subscribeRepository.existsBySubscribingUserIdAndSubscribedUserId(userId, subscribingUser.getId()))
                .collect(Collectors.toList());
    }
}
