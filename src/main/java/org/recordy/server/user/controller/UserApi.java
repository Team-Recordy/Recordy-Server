package org.recordy.server.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.recordy.server.auth.security.UserId;
import org.recordy.server.common.message.ErrorMessage;
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
            summary = "유저 로그인 API",
            description = "사용자의 로그인 요청을 처리합니다.",
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
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - 액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - 카카오 액세스 토큰의 정보를 조회하는 과정에서 오류가 발생하였습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - Apple OAuth Identity Token 토큰의 유효 기간이 만료되었습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - Apple OAuth Identity Token 토큰의 형식이 올바르지 않습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - Apple OAuth Identity Token의 클레임 값이 올바르지 않습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - Apple OAuth Identity Token의 값이 올바르지 않습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - Apple jwt 토큰이 유효하지 않습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - Apple OAuth 통신 암호화 과정 중 문제가 발생했습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - Apple 로그인 중 public key 생성에 문제가 발생했습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error - 서버 내부 오류입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
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
            summary = "유저 회원 가입 API",
            description = "소셜 로그인 이후 닉네임 설정 및 동의 항목 체크하는 API입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "요청이 성공했습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - 액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - 액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - 이미 존재하는 닉네임입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - 존재하지 않는 회원입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<Void> signUp(
            @UserId Long userId,
            @RequestBody UserSignUpRequest request
    );

    @Operation(
            summary = "닉네임 중복 체크 API",
            description = "닉네임 중복체크하는 API입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "요청이 성공했습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - 액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - 액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - 이미 존재하는 닉네임입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<Void> checkDuplicateNickname(
            @RequestParam String nickname
    );

    @Operation(
            summary = "토큰 재발급 API",
            description = "사용자의 토큰 리프레시 요청을 처리합니다. (refreshToken을 바탕으로 AccessToken을 재발급합니다.)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "요청이 성공했습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = UserReissueTokenResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - 액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - 존재하지 않는 회원입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - 인증 정보를 찾을 수 없습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<UserReissueTokenResponse> reissueToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken
    );

    @Operation(
            summary = "유저 회원 탈퇴 API",
            description = "유저가 회원 탈퇴하는 API입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "요청이 성공했습니다. 내용이 없습니다.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - 액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - 액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - 존재하지 않는 회원입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - 인증 정보를 찾을 수 없습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error - 서버 내부 오류입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<Void> delete(
            @UserId Long userId
    );

    @Operation(
            summary = "유저 로그아웃 API",
            description = "유저의 로그아웃 요청을 처리합니다. 이후 logout에 사용한 accessToken을 클라이언트 내에서 삭제해주세요.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "요청이 성공했습니다. 내용이 없습니다.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - 액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - 액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - 존재하지 않는 회원입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - 인증 정보를 찾을 수 없습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    )
            }
    )
    public ResponseEntity signOut(
            @UserId Long userId
    );
}