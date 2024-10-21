package org.recordy.server.record.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.recordy.server.common.dto.response.CursorBasePaginatedResponse;
import org.recordy.server.auth.security.resolver.UserId;
import org.recordy.server.common.dto.response.ErrorResponse;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.record.controller.dto.request.RecordCreateRequest;
import org.recordy.server.record.controller.dto.response.RecordGetResponse;
import org.recordy.server.record.domain.FileUrl;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "게시물(영상) API")
public interface RecordApi {

    @Operation(
            summary = "게시물(영상) 등록 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = Void.class
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
                            description = "존재하지 않는 장소입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "404 NOT FOUND",
                                                                "errorMessage": "존재하지 않는 장소입니다."
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
    public ResponseEntity<Void> create(
            @Parameter(
                    name = "액세스 토큰",
                    in = ParameterIn.HEADER,
                    required = true,
                    schema = @Schema(
                            implementation = String.class
                    )
            ) @UserId Long uploaderId,
            @RequestBody RecordCreateRequest request
    );

    @Operation(
            summary = "게시물(영상) 삭제 API",
            description = "사용자가 게시물(영상)의 소유자일 경우 해당 게시물(영상)을 삭제합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200"
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
                            responseCode = "403",
                            description = "삭제가 불가능한 기록입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "403 FORBIDDEN",
                                                                "errorMessage": "삭제가 불가능한 기록입니다."
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
                            description = "존재하지 않는 레코드입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "errorCode": "404 NOT FOUND",
                                                                "errorMessage": "존재하지 않는 레코드입니다."
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
    ResponseEntity<Void> delete(
            @Parameter(
                    name = "액세스 토큰",
                    in = ParameterIn.HEADER,
                    required = true,
                    schema = @Schema(
                            implementation = String.class
                    )
            ) @UserId Long uploaderId,
            @PathVariable Long recordId
    );

    @Operation(
            summary = "장소와 연관된 게시물(영상) 리스트 조회 API",
            description = "place_id를 받아 연관된 게시물(영상) 리스트를 최신순으로 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "nextCursor": 11,
                                                                "hasNext": true,
                                                                "content": [
                                                                    {
                                                                        "id": 1,
                                                                        "fileUrl":
                                                                            {
                                                                                "videoUrl": ""http://www.naver.com",
                                                                                "thumbnailUrl": ""http://www.naver.com"
                                                                            },
                                                                        "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                        "uploaderId": 1,
                                                                        "uploaderNickname": "konu",
                                                                        "bookmarkCount": 100,
                                                                        "isMine": true,
                                                                        "isBookmarked": false
                                                                    },
                                                                    {
                                                                        "id": 1,
                                                                        "fileUrl":
                                                                            {
                                                                                "videoUrl": ""http://www.naver.com",
                                                                                "thumbnailUrl": ""http://www.naver.com"
                                                                            },
                                                                        "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                        "uploaderId": 1,
                                                                        "uploaderNickname": "konu",
                                                                        "bookmarkCount": 100,
                                                                        "isMine": true,
                                                                        "isBookmarked": false
                                                                    },
                                                                    {
                                                                        "id": 1,
                                                                        "fileUrl":
                                                                            {
                                                                                "videoUrl": ""http://www.naver.com",
                                                                                "thumbnailUrl": ""http://www.naver.com"
                                                                            },
                                                                        "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                        "uploaderId": 1,
                                                                        "uploaderNickname": "konu",
                                                                        "bookmarkCount": 100,
                                                                        "isMine": true,
                                                                        "isBookmarked": false
                                                                    },
                                                                    {
                                                                        "id": 1,
                                                                        "fileUrl":
                                                                            {
                                                                                "videoUrl": ""http://www.naver.com",
                                                                                "thumbnailUrl": ""http://www.naver.com"
                                                                            },
                                                                        "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                        "uploaderId": 1,
                                                                        "uploaderNickname": "konu",
                                                                        "bookmarkCount": 100,
                                                                        "isMine": true,
                                                                        "isBookmarked": false
                                                                    },
                                                                    {
                                                                        "id": 1,
                                                                        "fileUrl":
                                                                            {
                                                                                "videoUrl": ""http://www.naver.com",
                                                                                "thumbnailUrl": ""http://www.naver.com"
                                                                            },
                                                                        "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                        "uploaderId": 1,
                                                                        "uploaderNickname": "konu",
                                                                        "bookmarkCount": 100,
                                                                        "isMine": true,
                                                                        "isBookmarked": false
                                                                    }
                                                                ]
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = RecordGetResponse.class
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
            }
    )
    ResponseEntity<CursorBasePaginatedResponse<RecordGetResponse>> getAllByPlace(
            @Parameter(
                    name = "액세스 토큰",
                    in = ParameterIn.HEADER,
                    required = true,
                    schema = @Schema(
                            implementation = String.class
                    )
            ) @UserId Long userId,
            @RequestParam Long placeId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    );

    @Operation(
            summary = "회원의 게시물(영상) 리스트 조회 API",
            description = "회원의 게시물(영상) 리스트를 최신순으로 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "nextCursor": 11,
                                                                "hasNext": true,
                                                                "content": [
                                                                    {
                                                                        "id": 1,
                                                                        "fileUrl": {
                                                                            "videoUrl": "http://www.naver.com",
                                                                            "thumbnailUrl": "http://www.naver.com"
                                                                        },
                                                                        "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                        "uploaderId": 1,
                                                                        "uploaderNickname": "konu",
                                                                        "bookmarkCount": 100,
                                                                        "isMine": true,
                                                                        "isBookmarked": false
                                                                    },
                                                                    {
                                                                        "id": 1,
                                                                        "fileUrl": {
                                                                            "videoUrl": "http://www.naver.com",
                                                                            "thumbnailUrl": "http://www.naver.com"
                                                                        },
                                                                        "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                        "uploaderId": 1,
                                                                        "uploaderNickname": "konu",
                                                                        "bookmarkCount": 100,
                                                                        "isMine": true,
                                                                        "isBookmarked": false
                                                                    },
                                                                    {
                                                                        "id": 1,
                                                                        "fileUrl": {
                                                                            "videoUrl": "http://www.naver.com",
                                                                            "thumbnailUrl": "http://www.naver.com"
                                                                        },
                                                                        "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                        "uploaderId": 1,
                                                                        "uploaderNickname": "konu",
                                                                        "bookmarkCount": 100,
                                                                        "isMine": true,
                                                                        "isBookmarked": false
                                                                    },
                                                                    {
                                                                        "id": 1,
                                                                        "fileUrl": {
                                                                            "videoUrl": "http://www.naver.com",
                                                                            "thumbnailUrl": "http://www.naver.com"
                                                                        },
                                                                        "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                        "uploaderId": 1,
                                                                        "uploaderNickname": "konu",
                                                                        "bookmarkCount": 100,
                                                                        "isMine": true,
                                                                        "isBookmarked": false
                                                                    },
                                                                    {
                                                                        "id": 1,
                                                                        "fileUrl": {
                                                                            "videoUrl": "http://www.naver.com",
                                                                            "thumbnailUrl": "http://www.naver.com"
                                                                        },
                                                                        "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                        "uploaderId": 1,
                                                                        "uploaderNickname": "konu",
                                                                        "bookmarkCount": 100,
                                                                        "isMine": true,
                                                                        "isBookmarked": false
                                                                    }
                                                                ]
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = RecordGetResponse.class
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
            }
    )
    ResponseEntity<CursorBasePaginatedResponse<RecordGetResponse>> getAllByUser(
            @Parameter(
                    name = "액세스 토큰",
                    in = ParameterIn.HEADER,
                    required = true,
                    schema = @Schema(
                            implementation = String.class
                    )
            ) @UserId Long userId,
            @PathVariable Long otherUserId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    );

    @Operation(
            summary = "팔로잉 사용자의 게시물(영상) 랜덤 리스트 조회 API",
            description = "팔로우하는 사용자의 게시물(영상) 리스트를 랜덤으로 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            [
                                                                {
                                                                    "id": 1,
                                                                    "fileUrl": {
                                                                        "videoUrl": "http://www.naver.com",
                                                                        "thumbnailUrl": "http://www.naver.com"
                                                                    },
                                                                    "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                    "uploaderId": 1,
                                                                    "uploaderNickname": "konu",
                                                                    "bookmarkCount": 100,
                                                                    "isMine": true,
                                                                    "isBookmarked": false
                                                                },
                                                                {
                                                                    "id": 1,
                                                                    "fileUrl": {
                                                                        "videoUrl": "http://www.naver.com",
                                                                        "thumbnailUrl": "http://www.naver.com"
                                                                    },
                                                                    "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                    "uploaderId": 1,
                                                                    "uploaderNickname": "konu",
                                                                    "bookmarkCount": 100,
                                                                    "isMine": true,
                                                                    "isBookmarked": false
                                                                },
                                                                {
                                                                    "id": 1,
                                                                    "fileUrl": {
                                                                        "videoUrl": "http://www.naver.com",
                                                                        "thumbnailUrl": "http://www.naver.com"
                                                                    },
                                                                    "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                    "uploaderId": 1,
                                                                    "uploaderNickname": "konu",
                                                                    "bookmarkCount": 100,
                                                                    "isMine": true,
                                                                    "isBookmarked": false
                                                                },
                                                                {
                                                                    "id": 1,
                                                                    "fileUrl": {
                                                                        "videoUrl": "http://www.naver.com",
                                                                        "thumbnailUrl": "http://www.naver.com"
                                                                    },
                                                                    "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                    "uploaderId": 1,
                                                                    "uploaderNickname": "konu",
                                                                    "bookmarkCount": 100,
                                                                    "isMine": true,
                                                                    "isBookmarked": false
                                                                },
                                                                {
                                                                    "id": 1,
                                                                    "fileUrl": {
                                                                        "videoUrl": "http://www.naver.com",
                                                                        "thumbnailUrl": "http://www.naver.com"
                                                                    },
                                                                    "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                    "uploaderId": 1,
                                                                    "uploaderNickname": "konu",
                                                                    "bookmarkCount": 100,
                                                                    "isMine": true,
                                                                    "isBookmarked": false
                                                                }
                                                            ]
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = RecordGetResponse.class
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
            }
    )
    ResponseEntity<List<RecordGetResponse>> getRandomBySubscription(
            @Parameter(
                    name = "액세스 토큰",
                    in = ParameterIn.HEADER,
                    required = true,
                    schema = @Schema(
                            implementation = String.class
                    )
            ) @UserId Long userId,
            @RequestParam(required = false, defaultValue = "10") int size
    );

    @Operation(
            summary = "전체 랜덤 게시물(영상) 조회 API",
            description = "전체 중 랜덤으로 게시물(영상) 리스트를 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            [
                                                                {
                                                                    "id": 1,
                                                                    "fileUrl": {
                                                                        "videoUrl": "http://www.naver.com",
                                                                        "thumbnailUrl": "http://www.naver.com"
                                                                    },
                                                                    "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                    "uploaderId": 1,
                                                                    "uploaderNickname": "konu",
                                                                    "bookmarkCount": 100,
                                                                    "isMine": true,
                                                                    "isBookmarked": false
                                                                },
                                                                {
                                                                    "id": 1,
                                                                    "fileUrl": {
                                                                        "videoUrl": "http://www.naver.com",
                                                                        "thumbnailUrl": "http://www.naver.com"
                                                                    },
                                                                    "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                    "uploaderId": 1,
                                                                    "uploaderNickname": "konu",
                                                                    "bookmarkCount": 100,
                                                                    "isMine": true,
                                                                    "isBookmarked": false
                                                                },
                                                                {
                                                                    "id": 1,
                                                                    "fileUrl": {
                                                                        "videoUrl": "http://www.naver.com",
                                                                        "thumbnailUrl": "http://www.naver.com"
                                                                    },
                                                                    "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                    "uploaderId": 1,
                                                                    "uploaderNickname": "konu",
                                                                    "bookmarkCount": 100,
                                                                    "isMine": true,
                                                                    "isBookmarked": false
                                                                },
                                                                {
                                                                    "id": 1,
                                                                    "fileUrl": {
                                                                        "videoUrl": "http://www.naver.com",
                                                                        "thumbnailUrl": "http://www.naver.com"
                                                                    },
                                                                    "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                    "uploaderId": 1,
                                                                    "uploaderNickname": "konu",
                                                                    "bookmarkCount": 100,
                                                                    "isMine": true,
                                                                    "isBookmarked": false
                                                                },
                                                                {
                                                                    "id": 1,
                                                                    "fileUrl": {
                                                                        "videoUrl": "http://www.naver.com",
                                                                        "thumbnailUrl": "http://www.naver.com"
                                                                    },
                                                                    "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                    "uploaderId": 1,
                                                                    "uploaderNickname": "konu",
                                                                    "bookmarkCount": 100,
                                                                    "isMine": true,
                                                                    "isBookmarked": false
                                                                }
                                                            ]
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = RecordGetResponse.class
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
            }
    )
    ResponseEntity<List<RecordGetResponse>> getRandom(
            @Parameter(
                    name = "액세스 토큰",
                    in = ParameterIn.HEADER,
                    required = true,
                    schema = @Schema(
                            implementation = String.class
                    )
            ) @UserId Long userId,
            @RequestParam(required = false, defaultValue = "10") int size
    );

    @Operation(
            summary = "북마크한 게시물(영상) 리스트 조회 API",
            description = "회원이 북마크한 게시물(영상) 리스트를 조회하는 API입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "nextCursor": 11,
                                                                "hasNext": true,
                                                                "content": [
                                                                    {
                                                                        "id": 1,
                                                                        "fileUrl": {
                                                                            "videoUrl": "http://www.naver.com",
                                                                            "thumbnailUrl": "http://www.naver.com"
                                                                        },
                                                                        "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                        "uploaderId": 1,
                                                                        "uploaderNickname": "konu",
                                                                        "bookmarkCount": 100,
                                                                        "isMine": true,
                                                                        "isBookmarked": false
                                                                    },
                                                                    {
                                                                        "id": 1,
                                                                        "fileUrl": {
                                                                            "videoUrl": "http://www.naver.com",
                                                                            "thumbnailUrl": "http://www.naver.com"
                                                                        },
                                                                        "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                        "uploaderId": 1,
                                                                        "uploaderNickname": "konu",
                                                                        "bookmarkCount": 100,
                                                                        "isMine": true,
                                                                        "isBookmarked": false
                                                                    },
                                                                    {
                                                                        "id": 1,
                                                                        "fileUrl": {
                                                                            "videoUrl": "http://www.naver.com",
                                                                            "thumbnailUrl": "http://www.naver.com"
                                                                        },
                                                                        "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                        "uploaderId": 1,
                                                                        "uploaderNickname": "konu",
                                                                        "bookmarkCount": 100,
                                                                        "isMine": true,
                                                                        "isBookmarked": false
                                                                    },
                                                                    {
                                                                        "id": 1,
                                                                        "fileUrl": {
                                                                            "videoUrl": "http://www.naver.com",
                                                                            "thumbnailUrl": "http://www.naver.com"
                                                                        },
                                                                        "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                        "uploaderId": 1,
                                                                        "uploaderNickname": "konu",
                                                                        "bookmarkCount": 100,
                                                                        "isMine": true,
                                                                        "isBookmarked": false
                                                                    },
                                                                    {
                                                                        "id": 1,
                                                                        "fileUrl": {
                                                                            "videoUrl": "http://www.naver.com",
                                                                            "thumbnailUrl": "http://www.naver.com"
                                                                        },
                                                                        "content": "정작 필요했던 말들은 다 덮어두고서",
                                                                        "uploaderId": 1,
                                                                        "uploaderNickname": "konu",
                                                                        "bookmarkCount": 100,
                                                                        "isMine": true,
                                                                        "isBookmarked": false
                                                                    }
                                                                ]
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = RecordGetResponse.class
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
            }
    )
    public ResponseEntity<CursorBasePaginatedResponse<RecordGetResponse>> getAllBookmarked(
            @Parameter(
                    name = "액세스 토큰",
                    in = ParameterIn.HEADER,
                    required = true,
                    schema = @Schema(
                            implementation = String.class
                    )
            ) @UserId Long userId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    );

    @Operation(
            summary = "presigned URL 발급 API",
            description = "영상과 썸네일 각각에 대한 presigned URL 발급합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "videoUrl": "http://www.naver.com",
                                                                "thumbnailUrl": "http://www.naver.com"
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = RecordGetResponse.class
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
    ResponseEntity<FileUrl> getPresignedFileUrl();
}
