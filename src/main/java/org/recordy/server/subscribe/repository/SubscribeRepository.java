package org.recordy.server.subscribe.repository;

import org.recordy.server.subscribe.domain.Subscribe;

import java.util.List;

public interface SubscribeRepository {

    // command
    void save(long subscribingUserId, long subscribedUserId);
    void delete(long subscribingUserId, long subscribedUserId);

    // query
    List<Subscribe> findAllBySubscribingUserId(long subscribingUserId);
    int countSubscribingUsers(long subscribedUserId);
    int countSubscribedUsers(long subscribingUserId);
    boolean existsBySubscribingUserIdAndSubscribedUserId(long subscribingUserId, long subscribedUserId);
}
