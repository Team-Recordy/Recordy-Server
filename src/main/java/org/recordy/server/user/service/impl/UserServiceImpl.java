package org.recordy.server.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.auth.security.UserAuthentication;
import org.recordy.server.auth.service.AuthService;
import org.recordy.server.auth.service.AuthTokenService;
import org.recordy.server.auth.service.impl.token.AuthTokenGenerator;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.user.domain.usecase.UserSignIn;
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
    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[가-힣0-9_.]+$");
    private final AuthTokenService authTokenService;


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
        UserStatus status = checkTermAllTrue(userSignUpRequest.termsAgreement());
        User updatedUser = existingUser.activate(
                userSignUpRequest.nickname(),
                status,
                userSignUpRequest.termsAgreement()
        );
        return userRepository.save(updatedUser);
    }

    @Override
    public void validateDuplicateNickname(String nickname) {
        if (userRepository.existsByNickname(nickname))
            throw new UserException(ErrorMessage.DUPLICATE_NICKNAME);
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
    public String reissueToken(String refreshToken) {
        String platformId = authTokenService.getPlatformIdFromRefreshToken(refreshToken);
        Long userId = getByPlatformId(platformId)
                .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND))
                .getId();

        return authTokenService.issueAccessToken(userId);
    }

    public void validateNicknameFormat(String nickname) {
        if (!NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new UserException(ErrorMessage.INVALID_NICKNAME_FORMAT);
        }
    }

    public UserStatus checkTermAllTrue(TermsAgreement termsAgreement) {
        if (termsAgreement.ageTerm() && termsAgreement.useTerm() && termsAgreement.personalInfoTerm()) {
            return UserStatus.ACTIVE;
        }
        throw new UserException(ErrorMessage.INVALID_REQUEST_TERM);

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
    public Optional<User> getByPlatformId(String platformId) {
        return userRepository.findByPlatformId(platformId);
    }

    @Override
    public Optional<User> getById(long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public void validateDuplicateNickname(String nickname) {
        if (userRepository.existsByNickname(nickname))
            throw new UserException(ErrorMessage.DUPLICATE_NICKNAME);
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
