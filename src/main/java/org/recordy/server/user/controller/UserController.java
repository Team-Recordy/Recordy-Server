package org.recordy.server.user.controller;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.usecase.AuthSignIn;
import org.recordy.server.auth.service.AuthService;
import org.recordy.server.auth.service.AuthTokenService;
import org.recordy.server.user.controller.dto.request.UserSignInRequest;
import org.recordy.server.user.controller.dto.response.UserReissueTokenResponse;
import org.recordy.server.user.controller.dto.response.UserSignInResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController implements UserApi{

    private final AuthService authService;
    private final AuthTokenService authTokenService;

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

    @Override
    @DeleteMapping("/logout")
    public ResponseEntity signOut(@AuthenticationPrincipal Principal principal) {
        long userId = Long.parseLong(principal.getName());
        authService.signOut(userId);
        return  ResponseEntity
                .noContent()
                .build();
    }


    @Override
    @GetMapping("/token")
    public ResponseEntity<UserReissueTokenResponse> reissueToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserReissueTokenResponse.of(
                        authTokenService.reissueToken(refreshToken)
                ));
    }

}
