package org.recordy.server.user.controller;


import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.security.UserId;
import org.recordy.server.user.controller.dto.request.UserSignUpRequest;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.user.controller.dto.request.UserSignInRequest;
import org.recordy.server.user.controller.dto.response.UserReissueTokenResponse;
import org.recordy.server.user.controller.dto.response.UserSignInResponse;
import org.recordy.server.user.domain.usecase.UserSignUp;
import org.recordy.server.user.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    @PostMapping("/signIn")
    public ResponseEntity<UserSignInResponse> signIn(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String platformToken,
            @RequestBody UserSignInRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserSignInResponse.from(
                        userService.signIn(UserSignIn.of(platformToken, request.platformType()))
                ));
    }

    @Override
    @PostMapping("/signUp")
    public ResponseEntity<Void> signUp(
            @UserId Long userId,
            @RequestBody UserSignUpRequest request
    ) {
        userService.signUp(UserSignUp.of(userId, request.nickname(), request.termsAgreement()));

        return ResponseEntity.
                status(HttpStatus.CREATED).
                build();
    }

    @Override
    @GetMapping("/check-nickname")
    public ResponseEntity<Void> checkDuplicateNickname(
            @RequestParam String nickname
    ) {
        userService.validateDuplicateNickname(nickname);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @PostMapping("/token")
    public ResponseEntity<UserReissueTokenResponse> reissueToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserReissueTokenResponse.of(
                        userService.reissueToken(refreshToken)
                ));
    }

    @Override
    @DeleteMapping("/logout")
    public ResponseEntity<Void> signOut(
            @UserId Long userId
    ) {
        userService.signOut(userId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Void> delete(
            @UserId Long userId
    ) {
        userService.delete(userId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
