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
        users.put(autoIncrementId, user);

        return User.builder()
                .id(autoIncrementId++)
                .authPlatform(user.getAuthPlatform())
                .status(user.getStatus())
                .build();
    }

    @Override
    public Optional<User> findByPlatformId(String platformId) {
        return users.values().stream()
                .filter(user -> user.getAuthPlatform().getId().equals(platformId))
                .findFirst();
    }
}
