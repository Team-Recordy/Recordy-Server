package org.recordy.server.user.repository;

import org.recordy.server.user.controller.dto.response.UserInfo;
import org.recordy.server.user.domain.User;
import org.springframework.data.domain.Slice;

public interface UserRepository {

    // command
    User save(User user);
    void deleteById(long id);

    // query
    User findById(long id);
    User findByPlatformId(String platformId);
    boolean existsByNickname(String nickname);
    Slice<UserInfo> findFollowings(long userId, Long cursor, int size);
    Slice<UserInfo> findFollowers(long userId, Long cursor, int size);
}
