package org.recordy.server.subscribe.service;

import java.util.List;
import org.recordy.server.subscribe.domain.usecase.SubscribeCreate;
import org.recordy.server.user.domain.User;
import org.springframework.data.domain.Slice;

public interface SubscribeService {

    // command
    boolean subscribe(SubscribeCreate subscribeCreate);

    // query
    Slice<User> getSubscribedUsers(long subscribingUserId, Long cursor, int size);
    Slice<User> getSubscribingUsers(long subscribedUserId, Long cursor, int size);
    List<Boolean> findSubscribes(long userId, Slice<User> subscribingUsers);
}
