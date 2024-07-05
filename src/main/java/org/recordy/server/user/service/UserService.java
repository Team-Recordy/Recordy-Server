package org.recordy.server.user.service;

import org.recordy.server.auth.domain.Auth;
import org.recordy.server.user.controller.dto.request.UserSignUpRequest;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.user.domain.User;

import java.util.Optional;

public interface UserService {

    // command
    Auth signIn(UserSignIn userSignIn);
    void signOut(long userId);
    User signUp(UserSignUpRequest userSignUpRequest);
    void delete(long userId);
    String reissueToken(String refreshToken);

    // query
    Optional<User> getByPlatformId(String platformId);
    void validateDuplicateNickname(String nickname);
    void validateNicknameFormat(String nickname);
}
