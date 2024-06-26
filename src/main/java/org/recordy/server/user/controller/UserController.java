package org.recordy.server.user.controller;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.usecase.AuthSignIn;
import org.recordy.server.auth.service.AuthService;
import org.recordy.server.user.controller.dto.request.UserSignInRequest;
import org.recordy.server.user.controller.dto.response.UserSignInResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final AuthService authService;

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
}
