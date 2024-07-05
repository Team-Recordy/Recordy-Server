package org.recordy.server.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.service.AuthService;
import org.recordy.server.auth.service.AuthTokenService;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.user.domain.usecase.UserSignUp;
import org.recordy.server.user.exception.UserException;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[가-힣0-9_.]+$");
    private final AuthTokenService authTokenService;

    @Override
    public Auth signIn(UserSignIn userSignIn) {
        AuthPlatform platform = authService.getPlatform(userSignIn);
        User user = getOrCreateUser(platform);

        return authService.create(user, platform);
    }

    private User getOrCreateUser(AuthPlatform platform) {
        return getByPlatformId(platform.getId())
                .orElseGet(() -> create(platform));
    }

    private User create(AuthPlatform platform) {
        return userRepository.save(User.builder()
                .authPlatform(platform)
                .status(UserStatus.PENDING)
                .build());
    }

    @Override
    public User signUp(UserSignUp userSignUp) {
        validateDuplicateNickname(userSignUp.nickname());

        User pendingUser = userRepository.findById(userSignUp.userId())
                .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND));
        User updatedUser = pendingUser.activate(userSignUp);

        return userRepository.save(updatedUser);
    }

    @Override
    public String reissueToken(String refreshToken) {
        String platformId = authTokenService.getPlatformIdFromRefreshToken(refreshToken);
        Long userId = getByPlatformId(platformId)
                .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND))
                .getId();

        return authTokenService.issueAccessToken(userId);
    }

    @Override
    public void signOut(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND));

        authService.signOut(user.getAuthPlatform().getId());
    }

    // TODO: 영상 도메인 추가되면 관련된 영상 및 시청기록도 삭제
    @Override
    public void delete(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND));

        authService.signOut(user.getAuthPlatform().getId());
        userRepository.deleteById(userId);
    }

    @Override
    public Optional<User> getByPlatformId(String platformId) {
        return userRepository.findByPlatformId(platformId);
    }

    @Override
    public Optional<User> getById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public void validateDuplicateNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new UserException(ErrorMessage.DUPLICATE_NICKNAME);
        }
    }
}
