package org.recordy.server.common.message;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ErrorMessage {

    /**
     * 401 Unauthorized
     */
    APPLE_INVAILD_JWT(HttpStatus.UNAUTHORIZED, "Apple jwt 토큰이 유효하지 않습니다."),
    APPLE_INVALID_IDENTITY_TOKEN(HttpStatus.UNAUTHORIZED, "Apple OAuth Identity Token 토큰의 형식이 올바르지 않습니다."),
    APPLE_UNABLE_TO_CREATE_PUBLIC_KEY(HttpStatus.UNAUTHORIZED, "Apple 로그인 중 public key 생성에 문제가 발생했습니다."),
    APPLE_EXPIRED_IDENTITY_TOKEN(HttpStatus.UNAUTHORIZED, "Apple OAuth Identity Token 토큰의 유효 기간이 만료되었습니다."),
    APPLE_INVALID_IDENTITY_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "Apple OAuth Identity Token의 값이 올바르지 않습니다."),
    APPLE_OAUTH_ENCRYPTION_ERROR(HttpStatus.UNAUTHORIZED,"Apple OAuth 통신 암호화 과정 중 문제가 발생했습니다."),
    APPLE_INVALID_IDENTITY_TOKEN_CLAIMS(HttpStatus.UNAUTHORIZED, "Apple OAuth Identity Token의 클레임 값이 올바르지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "리소스 접근 권한이 없습니다."),
    INVALID_KAKAO_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "카카오 액세스 토큰의 정보를 조회하는 과정에서 오류가 발생하였습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요."),
    INVALID_ACCESS_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "액세스 토큰의 값이 올바르지 않습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰이 만료되었습니다. 재발급 받아주세요."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰의 형식이 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "리프레시 토큰의 값이 올바르지 않습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다. 다시 로그인해 주세요."),
    NOT_MATCH_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "일치하지 않는 리프레시 토큰입니다."),

    /**
     * 404 Not Found
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    AUTH_NOT_FOUND(HttpStatus.NOT_FOUND, "인증 정보를 찾을 수 없습니다."),

    /**
     * 409 Conflict
     */
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    DUPLICATE_USER(HttpStatus.CONFLICT, "이미 존재하는 회원입니다."),

    /**
     * 500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

