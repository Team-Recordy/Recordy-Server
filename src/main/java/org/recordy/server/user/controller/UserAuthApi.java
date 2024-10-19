package org.recordy.server.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.recordy.server.auth.security.resolver.UserId;
import org.recordy.server.common.dto.response.ErrorResponse;
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

@Tag(name = "사용자 보안 및 인증 API")
public interface UserAuthApi {

    @Operation(
            summary = "사용자 로그인 API",
            description = "사용자가 플랫폼(카카오, 애플)의 토큰을 발급 받아 로그인합니다.",
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
                            responseCode = "400",
                            description = "요청 값이 유효하지 않습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "400 BAD REQUEST",
                                                                "errorMessage": "요청 값이 유효하지 않습니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "500 INTERNAL SERVER ERROR",
                                                                "errorMessage": "서버 내부 오류입니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "카카오 액세스 토큰의 정보를 조회하는 과정에서 오류가 발생하였습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "401 UNAUTHORIZED",
                                                                "errorMessage": "카카오 액세스 토큰의 정보를 조회하는 과정에서 오류가 발생하였습니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
            }
    )
    public ResponseEntity<UserSignInResponse> signIn(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String platformToken,
            @RequestBody UserSignInRequest request
    );

    @Operation(
            summary = "로그아웃 API",
            description = "액세스 토큰을 받아 로그아웃합니다.",
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
                    ),
                    @ApiResponse(
                            responseCode = "401 (1)",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "401 UNAUTHORIZED",
                                                                "errorMessage": "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401 (2)",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "401 UNAUTHORIZED",
                                                                "errorMessage": "액세스 토큰이 만료되었습니다. 재발급 받아주세요."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401 (3)",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "401 UNAUTHORIZED",
                                                                "errorMessage": "액세스 토큰의 값이 올바르지 않습니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "500 INTERNAL SERVER ERROR",
                                                                "errorMessage": "서버 내부 오류입니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404 (1)",
                            description = "존재하지 않는 회원입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "404 NOT FOUND",
                                                                "errorMessage": "존재하지 않는 회원입니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404 (2)",
                            description = "인증 정보를 찾을 수 없습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "404 NOT FOUND",
                                                                "errorMessage": "인증 정보를 찾을 수 없습니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    )
            }
    )
    public ResponseEntity signOut(
            @Parameter(
                    name = "액세스 토큰",
                    in = ParameterIn.HEADER,
                    required = true,
                    schema = @Schema(
                            implementation = String.class
                    )
            ) @UserId Long userId
    );

    @Operation(
            summary = "사용자 닉네임 중복체크 API",
            description = "사용자가 회원 가입할 때 닉네임을 중복체크합니다.",
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
                    ),
                    @ApiResponse(
                            responseCode = "401 (1)",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "401 UNAUTHORIZED",
                                                                "errorMessage": "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401 (2)",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "401 UNAUTHORIZED",
                                                                "errorMessage": "액세스 토큰이 만료되었습니다. 재발급 받아주세요."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401 (3)",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "401 UNAUTHORIZED",
                                                                "errorMessage": "액세스 토큰의 값이 올바르지 않습니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "500 INTERNAL SERVER ERROR",
                                                                "errorMessage": "서버 내부 오류입니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "이미 존재하는 닉네임입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "409 CONFLICT",
                                                                "errorMessage": "이미 존재하는 닉네임입니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
            }
    )
    @Parameter(
            name = "액세스 토큰",
            in = ParameterIn.HEADER,
            required = true,
            schema = @Schema(
                    implementation = String.class
            )
    )
    public ResponseEntity<Void> checkDuplicateNickname(
            @RequestParam String nickname
    );

    @Operation(
            security = @SecurityRequirement(name = "Authorization"),
            summary = "액세스 토큰 재발급 API",
            description = "리프레시 토큰을 받아 확인하고, 액세스 토큰을 재발급 합니다.",
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
                            responseCode = "401 (1)",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "401 UNAUTHORIZED",
                                                                "errorMessage": "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401 (2)",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "401 UNAUTHORIZED",
                                                                "errorMessage": "액세스 토큰이 만료되었습니다. 재발급 받아주세요."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401 (3)",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "401 UNAUTHORIZED",
                                                                "errorMessage": "액세스 토큰의 값이 올바르지 않습니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "500 INTERNAL SERVER ERROR",
                                                                "errorMessage": "서버 내부 오류입니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404 (1)",
                            description = "존재하지 않는 회원입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "404 NOT FOUND",
                                                                "errorMessage": "존재하지 않는 회원입니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404 (2)",
                            description = "인증 정보를 찾을 수 없습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "404 NOT FOUND",
                                                                "errorMessage": "인증 정보를 찾을 수 없습니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<UserReissueTokenResponse> reissueToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken
    );

    @Operation(
            summary = "사용자 회원 탈퇴 API",
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
                    ),
                    @ApiResponse(
                            responseCode = "401 (1)",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "401 UNAUTHORIZED",
                                                                "errorMessage": "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401 (2)",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "401 UNAUTHORIZED",
                                                                "errorMessage": "액세스 토큰이 만료되었습니다. 재발급 받아주세요."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401 (3)",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "401 UNAUTHORIZED",
                                                                "errorMessage": "액세스 토큰의 값이 올바르지 않습니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "500 INTERNAL SERVER ERROR",
                                                                "errorMessage": "서버 내부 오류입니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "존재하지 않는 회원입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "404 NOT FOUND",
                                                                "errorMessage": "존재하지 않는 회원입니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
            }
    )
    public ResponseEntity<Void> delete(
            @Parameter(
                    name = "액세스 토큰",
                    in = ParameterIn.HEADER,
                    required = true,
                    schema = @Schema(
                            implementation = String.class
                    )
            ) @UserId Long userId
    );

    @Operation(
            summary = "사용자 등록 API",
            description = "액세스 토큰과 사용자 등록 정보를 받아 사용자를 가입시킵니다.",
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
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "요청 값 파라미터의 타입이 올바르지 않습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "400 BAD REQUEST",
                                                                "errorMessage": "요청 값 파라미터의 타입이 올바르지 않습니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401 (1)",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "401 UNAUTHORIZED",
                                                                "errorMessage": "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401 (2)",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "401 UNAUTHORIZED",
                                                                "errorMessage": "액세스 토큰이 만료되었습니다. 재발급 받아주세요."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401 (3)",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "401 UNAUTHORIZED",
                                                                "errorMessage": "액세스 토큰의 값이 올바르지 않습니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "500 INTERNAL SERVER ERROR",
                                                                "errorMessage": "서버 내부 오류입니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "존재하지 않는 회원입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "404 NOT FOUND",
                                                                "errorMessage": "존재하지 않는 회원입니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "유효하지 않은 닉네임 형식입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "400 BAD REQUEST",
                                                                "errorMessage": "유효하지 않은 닉네임 형식입니다."
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "필수 동의항목에 모두 동의해주세요",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "400 BAD REQUEST",
                                                                "errorMessage": "필수 동의항목에 모두 동의해주세요"
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    ),
            }
    )
    public ResponseEntity<Void> signUp(
            @Parameter(
                    name = "액세스 토큰",
                    in = ParameterIn.HEADER,
                    required = true,
                    schema = @Schema(
                            implementation = String.class
                    )
            ) @UserId Long userId,
            @RequestBody UserSignUpRequest request
    );
}