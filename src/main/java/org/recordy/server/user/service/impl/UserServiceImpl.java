package org.recordy.server.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.service.AuthService;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.user.controller.dto.request.UserSignUpRequest;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserEntity;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.user.domain.usecase.UserSignIn;
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

    @Override
    public Auth signIn(UserSignIn userSignIn) {
        AuthPlatform platform = authService.getPlatform(userSignIn);
        User user = getOrCreateUser(platform);

        return authService.create(user, platform);
    }

    @Override
    public User signUp(UserSignUpRequest userSignUpRequest) {
        User existingUser = userRepository.findById(userSignUpRequest.userId())
                .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND));
        validateDuplicateNickname(userSignUpRequest.nickname()); //닉네임 중복 다시 검사
        validateNicknameFormat(userSignUpRequest.nickname()); //닉네임 형식 검사
        UserStatus status = checkTermAllTrue(userSignUpRequest.useTerm(), userSignUpRequest.personalInfoTerm());
        User updatedUser = existingUser.activate(
                userSignUpRequest.nickname(),
                status,
                userSignUpRequest.useTerm(),
                userSignUpRequest.personalInfoTerm()
        );
        return userRepository.save(updatedUser);
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

        authService.signOut(user.getAuthPlatform().getId());
        userRepository.deleteById(userId);
    }

    public void validateNicknameFormat(String nickname) {
        if (!NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new UserException(ErrorMessage.INVALID_NICKNAME_FORMAT);
        }
    }

    public UserStatus checkTermAllTrue(boolean useTerm, boolean personalInfoTerm){
        if (useTerm && personalInfoTerm) {
            return UserStatus.ACTIVE;
        }
        throw new UserException(ErrorMessage.INVALID_REQUEST_TERM);

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

    public Optional<User> getByPlatformId(String platformId) {
        return userRepository.findByPlatformId(platformId);
    }


}
