package org.recordy.server.user.service;

import org.recordy.server.auth.domain.Auth;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.user.domain.User;

import java.util.Optional;

public interface UserService {

    // command
    Auth signIn(UserSignIn userSignIn);
    void delete(long userId);

    // query
    void validateDuplicateNickname(String nickname);
    void validateNicknameFormat(String nickname);
}
