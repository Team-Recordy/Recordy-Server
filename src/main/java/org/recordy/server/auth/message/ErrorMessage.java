package org.recordy.server.auth.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorMessage {
    APPLE_INVAILD_JWT(HttpStatus.UNAUTHORIZED, "Apple jwt 토큰이 유효하지 않습니다."),
    APPLE_INVALID_IDENTITY_TOKEN(HttpStatus.UNAUTHORIZED, "Apple OAuth Identity Token 토큰의 형식이 올바르지 않습니다."),
    APPLE_UNABLE_TO_CREATE_PUBLIC_KEY(HttpStatus.UNAUTHORIZED, "Apple 로그인 중 public key 생성에 문제가 발생했습니다."),
    APPLE_EXPIRED_IDENTITY_TOKEN(HttpStatus.UNAUTHORIZED, "Apple OAuth Identity Token 토큰의 유효 기간이 만료되었습니다."),
    APPLE_INVALID_IDENTITY_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "Apple OAuth Identity Token의 값이 올바르지 않습니다."),
    APPLE_OAUTH_ENCRYPTION_ERROR(HttpStatus.UNAUTHORIZED,"Apple OAuth 통신 암호화 과정 중 문제가 발생했습니다."),
    APPLE_INVALID_IDENTITY_TOKEN_CLAIMS(HttpStatus.UNAUTHORIZED, "Apple OAuth Identity Token의 클레임 값이 올바르지 않습니다."),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
