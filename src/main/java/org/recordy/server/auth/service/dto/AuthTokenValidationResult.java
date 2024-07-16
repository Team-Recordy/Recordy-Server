package org.recordy.server.auth.service.dto;

public enum AuthTokenValidationResult {
    VALID_TOKEN,            // 유효한 토큰
    INVALID_SIGNATURE,      // 유효하지 않은 서명
    INVALID_TOKEN,          // 유효하지 않은 토큰
    EXPIRED_TOKEN,          // 만료된 토큰
    UNSUPPORTED_TOKEN,      // 지원하지 않는 형식의 토큰
    EMPTY_TOKEN             // 빈 토큰
}
