package org.recordy.server.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    @Override
    public User create(AuthPlatform platform, UserStatus userStatus) {
        return null;
    }

    @Override
    public Optional<User> getByPlatformId(String platformId) {
        return Optional.empty();
    }
}
