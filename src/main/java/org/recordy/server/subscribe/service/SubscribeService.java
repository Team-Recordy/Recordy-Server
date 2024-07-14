package org.recordy.server.subscribe.service;

import org.recordy.server.subscribe.domain.usecase.SubscribeCreate;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.response.UserInfo;
import org.springframework.data.domain.Slice;

public interface SubscribeService {

    // command
    void subscribe(SubscribeCreate subscribeCreate);
    void unsubscribe(long subscribingUserId, long subscribedUserId);

    // query
    Slice<UserInfo> getSubscribedUserInfos(long subscribingUserId, long cursor, int size);
    Slice<UserInfo> getSubscribingUserInfos(long subscribedUserId, long cursor, int size);
}
