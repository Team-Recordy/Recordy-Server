package org.recordy.server.preference.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.recordy.server.auth.security.resolver.UserId;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.preference.domain.usecase.Preference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Preference Api")
public interface PreferenceApi {
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
}
