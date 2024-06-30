package org.recordy.server.user.controller;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.usecase.AuthSignIn;
import org.recordy.server.auth.service.AuthService;
import org.recordy.server.user.controller.dto.request.UserSignInRequest;
import org.recordy.server.user.controller.dto.response.UserSignInResponse;
import org.recordy.server.user.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController implements UserApi{

    private final AuthService authService;
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
                        authService.signIn(AuthSignIn.of(platformToken, request.platformType()))
                ));
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Void> checkDuplicateNickname(@RequestParam String nickname) {
        userService.validateDuplicateNickname(nickname);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
