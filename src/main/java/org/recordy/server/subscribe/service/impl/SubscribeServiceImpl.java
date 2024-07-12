package org.recordy.server.subscribe.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.subscribe.domain.Subscribe;
import org.recordy.server.subscribe.domain.usecase.SubscribeCreate;
import org.recordy.server.subscribe.repository.SubscribeRepository;
import org.recordy.server.subscribe.service.SubscribeService;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.exception.UserException;
import org.recordy.server.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubscribeServiceImpl implements SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final UserRepository userRepository;

    @Override
    public void subscribe(SubscribeCreate subscribeCreate) {
        User subscribingUser = userRepository.findById(subscribeCreate.subscribingUserId())
                .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND));
        User subscribedUser = userRepository.findById(subscribeCreate.subscribedUserId())
                .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND));

        subscribeRepository.save(Subscribe.builder()
                .subscribingUser(subscribingUser)
                .subscribedUser(subscribedUser)
                .build());
    }

    @Override
    public void unsubscribe(long subscribingUserId, long subscribedUserId) {
        subscribeRepository.delete(subscribingUserId, subscribedUserId);
    }

    @Override
    public Slice<User> getSubscribedUsers(long subscribingUserId, long cursor, int size) {
        return subscribeRepository.findAllBySubscribingUserId(subscribingUserId, cursor, PageRequest.ofSize(size))
                .map(Subscribe::getSubscribedUser);
    }
}
