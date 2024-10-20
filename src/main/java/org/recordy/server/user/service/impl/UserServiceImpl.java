package org.recordy.server.user.service.impl;

import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.service.AuthService;
import org.recordy.server.auth.service.AuthTokenService;
import org.recordy.server.bookmark.repository.BookmarkRepository;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.subscribe.domain.Subscribe;
import org.recordy.server.subscribe.repository.SubscribeRepository;
import org.recordy.server.user.controller.dto.response.UserInfo;
import org.recordy.server.user.domain.TermsAgreement;
import org.recordy.server.user.domain.usecase.UserProfile;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.user.domain.usecase.UserSignUp;
import org.recordy.server.user.domain.usecase.UserUpdate;
import org.recordy.server.user.exception.UserException;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

    private final Long rootUserId;
    private final UserRepository userRepository;
    private final SubscribeRepository subscribeRepository;
    private final RecordRepository recordRepository;
    private final BookmarkRepository bookmarkRepository;
    private final AuthService authService;
    private final AuthTokenService authTokenService;

    public UserServiceImpl(
            @Value("${user.root.id}") long rootUserId,
            UserRepository userRepository,
            SubscribeRepository subscribeRepository,
            RecordRepository recordRepository,
            BookmarkRepository bookmarkRepository,
            AuthService authService,
            AuthTokenService authTokenService) {
        this.rootUserId = rootUserId;
        this.userRepository = userRepository;
        this.subscribeRepository = subscribeRepository;
        this.recordRepository = recordRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.authService = authService;
        this.authTokenService = authTokenService;
    }

    @Transactional
    @Override
    public Auth signIn(UserSignIn userSignIn) {
        AuthPlatform platform = authService.getPlatform(userSignIn);
        User user = getOrCreateUser(platform);

        return authService.create(user, platform);
    }

    private User getOrCreateUser(AuthPlatform platform) {
        try {
            return userRepository.findByPlatformId(platform.getId());
        } catch (UserException e) {
            return create(platform);
        }
    }

    private User create(AuthPlatform platform) {
        return userRepository.save(User.builder()
                .authPlatform(platform)
                .status(UserStatus.PENDING)
                .termsAgreement(TermsAgreement.defaultAgreement())
                .build());
    }

    @Transactional
    @Override
    public User signUp(UserSignUp userSignUp) {
        validateDuplicateNickname(userSignUp.nickname());

        User pendingUser = userRepository.findById(userSignUp.userId());
        User updatedUser = pendingUser.activate(userSignUp);

        followRootUser(updatedUser);
        return userRepository.save(updatedUser);
    }

    private void followRootUser(User user) {
        if (!user.getId().equals(rootUserId)) {
            User rootUser = userRepository.findById(rootUserId);
            subscribeRepository.save(Subscribe.builder()
                    .subscribingUser(user)
                    .subscribedUser(rootUser)
                    .build());
        }
    }

    @Override
    public String reissueToken(String refreshToken) {
        String platformId = authTokenService.getPlatformIdFromRefreshToken(refreshToken);
        User user = userRepository.findByPlatformId(platformId);

        return authTokenService.issueAccessToken(user.getId());
    }

    @Override
    public void signOut(long userId) {
        User user = userRepository.findById(userId);

        authService.signOut(user.getAuthPlatform().getId());
    }

    @Transactional
    @Override
    public void update(UserUpdate update, long id) {
        validateDuplicateNickname(update.nickname());

        User user = userRepository.findById(id);
        user.update(update);

        userRepository.save(user);
    }

    @Transactional
    @Override
    public void delete(long userId) {
        User user = userRepository.findById(userId);

        subscribeRepository.deleteByUserId(userId);
        bookmarkRepository.deleteByUserId(userId);
        recordRepository.deleteByUserId(userId);
        authService.signOut(user.getAuthPlatform().getId());
        userRepository.deleteById(userId);
    }

    @Override
    public UserProfile getProfile(long targetUserId, long userId) {
        return userRepository.findProfile(targetUserId, userId);
    }

    @Override
    public void validateDuplicateNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new UserException(ErrorMessage.DUPLICATE_NICKNAME);
        }
    }

    @Override
    public Slice<UserInfo> getSubscribingUserInfos(long userId, Long cursor, int size) {
        return userRepository.findFollowings(userId, cursor, size);
    }

    @Override
    public Slice<UserInfo> getSubscribedUserInfos(long userId, Long cursor, int size) {
        return userRepository.findFollowers(userId, cursor, size);
    }
}
