package org.recordy.server.user.service;

import org.recordy.server.auth.domain.Auth;
import org.recordy.server.user.controller.dto.response.UserInfo;
import org.recordy.server.user.domain.usecase.UserProfile;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.usecase.UserSignUp;
import org.recordy.server.user.domain.usecase.UserUpdate;
import org.springframework.data.domain.Slice;

public interface UserService {

    // command
    Auth signIn(UserSignIn userSignIn);
    User signUp(UserSignUp userSignUp);
    String reissueToken(String refreshToken);
    void signOut(long userId);
    void update(UserUpdate update, long id);
    void delete(long userId);

    // query
    UserProfile getProfile(long targetUserId, long userId);
    void validateDuplicateNickname(String nickname);
    Slice<UserInfo> getSubscribingUserInfos(long userId, Long cursor, int size);
    Slice<UserInfo> getSubscribedUserInfos(long userId, Long cursor, int size);
}
