package org.recordy.server.subscribe.service;

import org.recordy.server.user.domain.User;

import java.util.List;

public interface SubscribeService {

    // command
    void subscribe(long subscribingUserId, long subscribedUserId);
    void unsubscribe(long subscribingUserId, long subscribedUserId);

    // query
    List<User> getSubscribedUsers(long subscribingUserId);
}
