package org.recordy.server.subscribe.service.impl;

import org.recordy.server.subscribe.service.SubscribeService;
import org.recordy.server.user.domain.User;

import java.util.List;

public class SubscribeServiceImpl implements SubscribeService {

    @Override
    public void subscribe(long subscribingUserId, long subscribedUserId) {

    }

    @Override
    public void unsubscribe(long subscribingUserId, long subscribedUserId) {

    }

    @Override
    public List<User> getSubscribedUsers(long subscribingUserId) {
        return List.of();
    }
}
