package org.recordy.server.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.user.exception.ConflictException;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(AuthPlatform platform) {
        return userRepository.save(User.builder()
                .authPlatform(platform)
                .status(UserStatus.PENDING)
                .build());
    }

    @Override
    public Optional<User> getByPlatformId(String platformId) {
        return userRepository.findByPlatformId(platformId);
    }

    @Override
    public void validateDuplicateNickname(String nickname) {
        if (userRepository.existsUserByNickname(nickname)) {
            throw new ConflictException(ErrorMessage.DUPLICATE_NICKNAME);
        }
    }



}
