package org.recordy.server.record.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.recordy.server.common.dto.response.CursorBasePaginatedResponse;
import org.recordy.server.auth.security.resolver.UserId;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.record.controller.dto.request.RecordCreateRequest;
import org.recordy.server.record.controller.dto.response.RecordGetResponse;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "기록 관련 API")
public interface RecordApi {

    @Operation(
            summary = "레코드 생성 API",
            description = "유저가 레코드를 생성하는 API입니다. 파일과 함께 레코드 정보를 전송합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "레코드가 성공적으로 생성되었습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - 요청이 잘못되었습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - 인증이 필요합니다.",
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
    public ResponseEntity<Void> create(
            @UserId Long uploaderId,
            @RequestBody RecordCreateRequest request);

    @Operation(
            summary = "레코드 삭제 API",
            description = "주어진 record_id의 소유자라면 해당 record_id에 해당하는 레코드를 삭제합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "레코드가 성공적으로 삭제되었습니다.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - 삭제가 불가능한 기록입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - 존재하지 않는 기록입니다.",
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
    ResponseEntity<Void> delete(
            @UserId Long uploaderId,
            @PathVariable Long recordId
    );

    @Operation(
            summary = "특정 장소와 연관된 레코드 리스트 조회 API",
            description = "특정 장소와 연관된 레코드 리스트를 최신순으로 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "요청이 성공적으로 처리되었습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = Slice.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - 인증이 필요합니다.",
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
    ResponseEntity<CursorBasePaginatedResponse<RecordGetResponse>> getAllByPlace(
            @UserId Long userId,
            @RequestParam Long placeId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    );

    @Operation(
            summary = "회원의 레코드 리스트 조회 API",
            description = "회원의 최근 레코드 리스트를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "요청이 성공적으로 처리되었습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = Slice.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - 인증이 필요합니다.",
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
    ResponseEntity<CursorBasePaginatedResponse<RecordGetResponse>> getAllByUser(
            @UserId Long userId,
            @PathVariable Long otherUserId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    );

    @Operation(
            summary = "구독 레코드 리스트 조회 API",
            description = "구독 중인 유저의 레코드 리스트를 최신순으로 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "요청이 성공적으로 처리되었습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = Slice.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - 인증이 필요합니다.",
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
    ResponseEntity<List<RecordGetResponse>> getRandomBySubscription(
            @UserId Long userId,
            @RequestParam(required = false, defaultValue = "10") int size
    );

    @Operation(
            summary = "전체 레코드 조회 API",
            description = "전체 레코드 중 정해진 크기만큼 랜덤으로 만든 레코드 리스트를 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "요청이 성공적으로 처리되었습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = Slice.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - 인증이 필요합니다.",
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
    ResponseEntity<List<RecordGetResponse>> getRandom(
            @UserId Long userId,
            @RequestParam(required = false, defaultValue = "10") int size
    );

    @Operation(
            summary = "북마크된 기록 리스트 조회 API",
            description = "북마크된 기록 리스트를 조회하는 API입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "요청이 성공적으로 처리되었습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = Slice.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - 인증이 필요합니다.",
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
    public ResponseEntity<CursorBasePaginatedResponse<RecordGetResponse>> getAllBookmarked(
            @UserId Long userId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    );
}
