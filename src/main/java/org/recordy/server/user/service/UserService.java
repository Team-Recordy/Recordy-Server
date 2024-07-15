package org.recordy.server.user.service;

import org.recordy.server.auth.domain.Auth;
import org.recordy.server.user.domain.usecase.UserProfile;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.usecase.UserSignUp;

import java.util.Optional;

public interface UserService {

    // command
    Auth signIn(UserSignIn userSignIn);
    User signUp(UserSignUp userSignUp);
    String reissueToken(String refreshToken);
    void signOut(long userId);
    void delete(long userId);

    // query
    UserProfile getProfile(long id);
    Optional<User> getByPlatformId(String platformId);
    Optional<User> getById(long id);
    void validateDuplicateNickname(String nickname);
}
