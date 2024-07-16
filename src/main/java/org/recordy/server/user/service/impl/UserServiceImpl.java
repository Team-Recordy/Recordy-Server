package org.recordy.server.user.service.impl;

import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.service.AuthService;
import org.recordy.server.auth.service.AuthTokenService;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.subscribe.domain.Subscribe;
import org.recordy.server.subscribe.repository.SubscribeRepository;
import org.recordy.server.user.domain.TermsAgreement;
import org.recordy.server.user.domain.usecase.UserProfile;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.user.domain.usecase.UserSignUp;
import org.recordy.server.user.exception.UserException;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final Long rootUserId;

    private final UserRepository userRepository;
    private final SubscribeRepository subscribeRepository;
    private final RecordRepository recordRepository;
    private final AuthService authService;
    private final AuthTokenService authTokenService;

    public UserServiceImpl(
            @Value("${user.root.id}") Long rootUserId,
            UserRepository userRepository,
            SubscribeRepository subscribeRepository,
            RecordRepository recordRepository,
            AuthService authService,
            AuthTokenService authTokenService
    ) {
        this.rootUserId = rootUserId;
        this.userRepository = userRepository;
        this.subscribeRepository = subscribeRepository;
        this.recordRepository = recordRepository;
        this.authService = authService;
        this.authTokenService = authTokenService;
    }

    @Override
    public Auth signIn(UserSignIn userSignIn) {
        AuthPlatform platform = authService.getPlatform(userSignIn);
        User user = getOrCreateUser(platform);

        return authService.create(user, platform);
    }

    private User getOrCreateUser(AuthPlatform platform) {
        return userRepository.findByPlatformId(platform.getId())
                .orElseGet(() -> create(platform));
    }

    private User create(AuthPlatform platform) {
        return userRepository.save(User.builder()
                .authPlatform(platform)
                .status(UserStatus.PENDING)
                .termsAgreement(TermsAgreement.defaultAgreement())
                .build());
    }

    @Override
    public User signUp(UserSignUp userSignUp) {
        validateDuplicateNickname(userSignUp.nickname());

        User pendingUser = userRepository.findById(userSignUp.userId())
                .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND));
        User updatedUser = pendingUser.activate(userSignUp);
        followRoot(updatedUser);

        return userRepository.save(updatedUser);
    }

    private void followRoot(User user) {
        if (!user.getId().equals(rootUserId)) {
            userRepository.findById(rootUserId)
                    .ifPresent(rootUser ->
                            subscribeRepository.save(Subscribe.builder()
                                    .subscribingUser(user)
                                    .subscribedUser(rootUser)
                                    .build())
                    );
        }
    }

    @Override
    public String reissueToken(String refreshToken) {
        String platformId = authTokenService.getPlatformIdFromRefreshToken(refreshToken);
        User user = userRepository.findByPlatformId(platformId)
                .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND));

        return authTokenService.issueAccessToken(user.getId());
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
        recordRepository.deleteByUserId(userId);
    }

    @Override
    public UserProfile getProfile(long userId, long otherUserId) {
        User user = userRepository.findById(otherUserId)
                .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND));
        long records = recordRepository.countAllByUserId(user.getId());
        long followers = subscribeRepository.countSubscribingUsers(user.getId());
        long followings = subscribeRepository.countSubscribedUsers(user.getId());
        boolean isFollowing = subscribeRepository.existsBySubscribingUserIdAndSubscribedUserId(userId, otherUserId);

        return UserProfile.of(user, records, followers, followings, isFollowing);
    }

    @Override
    public void validateDuplicateNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new UserException(ErrorMessage.DUPLICATE_NICKNAME);
        }
    }
}
