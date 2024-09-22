package org.recordy.server.mock.user;

import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.user.controller.dto.response.UserInfo;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.exception.UserException;
import org.recordy.server.user.repository.UserRepository;
import org.springframework.data.domain.Slice;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FakeUserRepository implements UserRepository {

    public long autoIncrementId = 1L;
    public final Map<Long, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        }

        User realUser = User.builder()
                .id(autoIncrementId)
                .authPlatform(user.getAuthPlatform())
                .status(user.getStatus())
                .nickname(user.getNickname())
                .termsAgreement(user.getTermsAgreement())
                .build();

        users.put(autoIncrementId++, realUser);
        return realUser;
    }

    @Override
    public void deleteById(long id) {
        users.remove(id);
    }

    @Override
    public User findById(long id) {
        return users.get(id);
    }

    @Override
    public User findByPlatformId(String platformId) {
        return users.values().stream()
                .filter(user -> user.getAuthPlatform().getId().equals(platformId))
                .findFirst()
                .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND));
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return users.values().stream()
                .anyMatch(user -> {
                    if (!Objects.isNull(user.getNickname()))
                        return user.getNickname().equals(nickname);

                    return false;
                });
    }

    @Override
    public Slice<UserInfo> findFollowings(long userId, Long cursor, int size) {
        return null;
    }

    @Override
    public Slice<UserInfo> findFollowers(long userId, Long cursor, int size) {
        return null;
    }
}
