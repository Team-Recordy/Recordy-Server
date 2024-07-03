package org.recordy.server.user.service;

import org.recordy.server.auth.domain.Auth;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.user.domain.User;

import java.util.Optional;

public interface UserService {

    // command
    Auth signIn(UserSignIn userSignIn);
    void signOut(long userId);
    void delete(long userId);
    String reissueToken(String refreshToken);

    // query
    Optional<User> getByPlatformId(String platformId);
    Optional<User> getById(long userId);
    void validateDuplicateNickname(String nickname);
}
