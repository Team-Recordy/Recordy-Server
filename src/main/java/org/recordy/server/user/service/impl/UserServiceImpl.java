package org.recordy.server.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.service.AuthService;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.user.exception.UserException;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthService authService;

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
        if (userRepository.existsByNickname(nickname))
            throw new UserException(ErrorMessage.DUPLICATE_NICKNAME);
    }

    // TODO: 영상 도메인 추가되면 관련된 영상 및 시청기록도 삭제
    @Override
    public void delete(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND));

        authService.deleteByPlatformId(user.getAuthPlatform().getId());
        userRepository.deleteById(userId);
    }
}
