package org.recordy.server.user.service;

import org.recordy.server.auth.domain.Auth;
import org.recordy.server.user.controller.dto.request.UserSignUpRequest;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.user.domain.User;

import java.util.Optional;

public interface UserService {

    // command
    Auth signIn(UserSignIn userSignIn);
    User signUp(UserSignUpRequest userSignUpRequest);
    String reissueToken(String refreshToken);
    void signOut(long userId);
    void delete(long userId);

    // query
    Optional<User> getByPlatformId(String platformId);
    void validateDuplicateNickname(String nickname);
}
