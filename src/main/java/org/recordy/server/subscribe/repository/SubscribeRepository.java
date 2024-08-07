package org.recordy.server.subscribe.repository;

import org.recordy.server.subscribe.domain.Subscribe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface SubscribeRepository {

    // command
    void save(Subscribe subscribe);
    void delete(long subscribingUserId, long subscribedUserId);
    void deleteByUserId(long userId);

    // query
    Slice<Subscribe> findAllBySubscribingUserId(long subscribingUserId, Long cursor, Pageable pageable);
    Slice<Subscribe> findAllBySubscribedUserId(long subscribedUserId, Long cursor, Pageable pageable);
    boolean existsBySubscribingUserIdAndSubscribedUserId(long subscribingUserId, long subscribedUserId);
    long countSubscribingUsers(long subscribedUserId);
    long countSubscribedUsers(long subscribingUserId);
}
