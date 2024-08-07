package org.recordy.server.subscribe.repository.impl;

import org.recordy.server.subscribe.domain.SubscribeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeJpaRepository extends JpaRepository<SubscribeEntity, Long> {

    void deleteAllBySubscribingUserIdAndSubscribedUserId(long subscribingUserId, long subscribedUserId);
    void deleteAllBySubscribedUserIdOrSubscribingUserId(long subscribingUserId, long subscribedUserId);
}
