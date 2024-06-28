package org.recordy.server.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.recordy.server.user.controller.dto.request.UserSignInRequest;
import org.recordy.server.user.controller.dto.response.UserSignInResponse;
import org.recordy.server.user.controller.dto.response.UserReissueTokenResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "유저 관련 API")
public interface UserApi {
    @Operation(
            summary = "유저 회원 가입 API",
            description = "유저가 회원 가입하는 API입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = UserSignInResponse.class
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<UserSignInResponse> signIn(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String platformToken,
            @RequestBody UserSignInRequest request
    );

    @Operation(
            summary = "access token 재발급 API",
            description = "refresh token을 바탕으로 access token을 재발급 받는 API입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = UserReissueTokenResponse.class
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<UserReissueTokenResponse> reissueToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken
    );

}