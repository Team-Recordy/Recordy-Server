package org.recordy.server.mock.user;

import org.recordy.server.user.domain.User;
import org.recordy.server.user.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeUserRepository implements UserRepository {

    private long autoIncrementId = 1L;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public Optional<User> findByPlatformId(String platformId) {
        return users.values().stream()
                .filter(user -> user.getAuthPlatform().getId().equals(platformId))
                .findFirst();
    }
}
