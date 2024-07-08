package org.recordy.server.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.recordy.server.auth.security.UserId;
import org.recordy.server.user.controller.dto.request.UserSignInRequest;
import org.recordy.server.user.controller.dto.request.UserSignUpRequest;
import org.recordy.server.user.controller.dto.response.UserSignInResponse;
import org.recordy.server.user.controller.dto.response.UserReissueTokenResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "유저 관련 API")
public interface UserApi {
    @Operation(
            summary = "유저 회원 가입 API",
            description = "유저가 회원 가입하는 API입니다. ",
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
            security = @SecurityRequirement(name = "Authorization"),
            summary = "로그아웃 API",
            description = "access token을 바탕으로 로그아웃을 진행하는 API입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "요청이 성공했습니다."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "존재하지 않는 회원입니다.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "인증 정보를 찾을 수 없습니다.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity signOut(
            @UserId Long userId
    );

    @Operation(
            security = @SecurityRequirement(name = "Authorization"),
            summary = "유저 닉네임 중복체크 API",
            description = "유저가 회원 가입할 때 닉네임을 중복체크해주는 API입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = void.class
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<Void> checkDuplicateNickname(
            @RequestParam String nickname
    );

    @Operation(
            summary = "유저 회원 탈퇴 API",
            description = "유저가 회원 탈퇴하는 API입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = void.class
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<Void> delete(
            @UserId Long userId
    );

    @Operation(
            security = @SecurityRequirement(name = "Authorization"),
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
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "존재하지 않는 회원입니다.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "인증 정보를 찾을 수 없습니다.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<UserReissueTokenResponse> reissueToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken
    );

    @Operation(
            summary = "유저 회원 등록 API",
            description = "유저 회원 등록하는 API입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = void.class
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<Void> signUp(
            @UserId Long userId,
            @RequestBody UserSignUpRequest request
    );
}