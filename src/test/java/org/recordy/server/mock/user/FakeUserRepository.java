package org.recordy.server.mock.user;

import org.recordy.server.user.domain.User;
import org.recordy.server.user.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeUserRepository implements UserRepository {

    public long autoIncrementId = 1L;
    public final Map<Long, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        if (users.get(user.getId()) != null) {
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
    public Optional<User> findByPlatformId(String platformId) {
        return users.values().stream()
                .filter(user -> user.getAuthPlatform().getId().equals(platformId))
                .findFirst();
    }

    @Override
    public void deleteById(long userId) {
        users.remove(userId);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return users.values().stream()
                .anyMatch(user -> {
                    if (user.getNickname() != null) {
                        return user.getNickname().equals(nickname);
                    }

                    return false;
                });
    }

    @Override
    public Optional<User> findById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }
}
