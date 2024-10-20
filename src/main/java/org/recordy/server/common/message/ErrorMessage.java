package org.recordy.server.common.message;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ErrorMessage {

    /**
     * COMMON
     */
    CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 리소스입니다."),
    ENUM_VALUE_BAD_REQUEST(HttpStatus.BAD_REQUEST, "요청한 값이 유효하지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    INVALID_REQUEST_BODY_TYPE(HttpStatus.BAD_REQUEST, "요청 값 파라미터의 타입이 올바르지 않습니다."),
    METHOD_NOT_SUPPORTED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메소드입니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 API 경로입니다."),
    VALIDATION_REQUEST_MISSING_EXCEPTION(HttpStatus.BAD_REQUEST, "요청 값이 유효하지 않습니다."),

    /**
     * EXTERNAL
     */
    INVALID_FILE_SIZE(HttpStatus.BAD_REQUEST,"파일 사이즈는 100MB를 넘을 수 없습니다."),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST,"이미지 확장자는 jpg, png, webp만, 비디오 확장자는 mp4, mov, quicktime만 가능합니다."),

    /**
     * AUTH
     */
    AUTH_NOT_FOUND(HttpStatus.NOT_FOUND, "인증 정보를 찾을 수 없습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰이 만료되었습니다. 재발급 받아주세요."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요."),
    INVALID_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "액세스 토큰의 값이 올바르지 않습니다."),

    // APPLE
    APPLE_EXPIRED_IDENTITY_TOKEN(HttpStatus.UNAUTHORIZED, "Apple OAuth Identity Token 토큰의 유효 기간이 만료되었습니다."),
    APPLE_INVALID_IDENTITY_TOKEN(HttpStatus.UNAUTHORIZED, "Apple OAuth Identity Token 토큰의 형식이 올바르지 않습니다."),
    APPLE_INVALID_IDENTITY_TOKEN_CLAIMS(HttpStatus.UNAUTHORIZED, "Apple OAuth Identity Token의 클레임 값이 올바르지 않습니다."),
    APPLE_INVALID_IDENTITY_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "Apple OAuth Identity Token의 값이 올바르지 않습니다."),
    APPLE_INVALID_JWT(HttpStatus.UNAUTHORIZED, "Apple jwt 토큰이 유효하지 않습니다."),
    APPLE_OAUTH_ENCRYPTION_ERROR(HttpStatus.UNAUTHORIZED, "Apple OAuth 통신 암호화 과정 중 문제가 발생했습니다."),
    APPLE_UNABLE_TO_CREATE_PUBLIC_KEY(HttpStatus.UNAUTHORIZED, "Apple 로그인 중 public key 생성에 문제가 발생했습니다."),

    // KAKAO
    INVALID_KAKAO_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "카카오 액세스 토큰의 정보를 조회하는 과정에서 오류가 발생하였습니다."),

    /**
     * USER
     */
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    DUPLICATE_USER(HttpStatus.CONFLICT, "이미 존재하는 회원입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    FAILED_TO_UPLOAD_TO_S3(HttpStatus.INTERNAL_SERVER_ERROR, "S3에 업로드를 실패했습니다."),
    INVALID_NICKNAME_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않은 닉네임 형식입니다."),
    INVALID_REQUEST_TERM(HttpStatus.BAD_REQUEST, "필수 동의항목에 모두 동의해주세요"),
    INVALID_IMAGE_URL_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않은 url 형식입니다."),

    /**
     * RECORD
     */
    FORBIDDEN_DELETE_RECORD(HttpStatus.FORBIDDEN, "삭제가 불가능한 기록입니다."),
    RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 레코드입니다."),

    /**
     * EXHIBITION
     */
    EXHIBITION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 전시회입니다."),

    /**
     * PLACE
     */
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 장소입니다."),
    PLATFORM_PLACE_NO_SEARCH_RESULT(HttpStatus.NOT_FOUND, "지도 검색 결과가 없습니다."),

    /**
     * SEARCH
     */
    INDEXING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "검색 서비스를 통해 문서를 인덱싱하는 데에 실패했습니다."),
    SEARCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "검색 서비스를 통해 검색하는 데에 실패했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

