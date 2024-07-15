package org.recordy.server.record_stat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.recordy.server.auth.security.UserId;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.controller.dto.response.RecordInfoWithBookmark;
import org.recordy.server.record_stat.domain.usecase.Preference;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "RecordStat Api")
public interface RecordStatApi {

    @Operation(
            summary = "레코드 북마크 API",
            description = "사용자가 특정 레코드를 북마크합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "요청이 성공적으로 처리되었습니다.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - 존재하지 않는 사용자 또는 레코드입니다.",
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
    public ResponseEntity<Void> bookmark(
            @UserId Long userId,
            @PathVariable Long recordId
    );

    @Operation(
            summary = "레코드 북마크 삭제 API",
            description = "사용자가 특정 레코드의 북마크를 삭제합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "요청이 성공적으로 처리되었습니다.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - 존재하지 않는 사용자 또는 레코드입니다.",
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
    public ResponseEntity<Void> deleteBookmark(
            @UserId Long userId,
            @PathVariable Long recordId
    );

    @Operation(
            security = @SecurityRequirement(name = "Authorization"),
            summary = "유저 취향 키워드 Top 3 API",
            description = "유저가 저장, 시청, 올린 영상을 바탕으로 유저 취향 키워드를 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = Preference.class
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
    public ResponseEntity<Preference> getPreference(
            @UserId Long userId
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
    public ResponseEntity<Slice<RecordInfoWithBookmark>> getBookmarkedRecords(
            @UserId Long userId,
            @RequestParam(required = false, defaultValue = "0L") long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    );


}
