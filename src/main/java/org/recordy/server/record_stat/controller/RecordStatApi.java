package org.recordy.server.record_stat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.recordy.server.auth.security.UserId;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record_stat.domain.usecase.Preference;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "통계 관련 API")
public interface RecordStatApi {

    @Operation(
            summary = "북마크 API",
            description = "북마크를 진행하는 API입니다. ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공"
                    )
            }
    )
    public ResponseEntity bookmark(
            @UserId Long userId,
            @PathVariable Long recordId
    );

    @Operation(
            security = @SecurityRequirement(name = "Authorization"),
            summary = "북마크 삭제 API",
            description = "access token을 바탕으로 로그아웃을 진행하는 API입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "요청이 성공했습니다."
                    )
            }
    )
    public ResponseEntity deleteBookmark(
            @UserId Long userId,
            @PathVariable Long recordId
    );

    @Operation(
            security = @SecurityRequirement(name = "Authorization"),
            summary = "유저 취향 키워드 top 3 API",
            description = "유저가 저장, 시청, 올린 영상을 바탕으로 유저 취향 키워드 top 3 API입니다.",
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
                            description = "성공",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = Slice.class
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<Slice<Record>> getBookmarkedRecords(
            @UserId Long userId,
            @RequestParam(required = false, defaultValue = "0L") long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    );


}
