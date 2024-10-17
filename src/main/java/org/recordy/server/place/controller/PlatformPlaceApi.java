package org.recordy.server.place.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.place.controller.dto.response.PlatformPlaceSearchResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PlatformPlaceApi {

    @Operation(
            summary = "플랫폼 장소 검색 API",
            description = "지도 플랫폼을 통해 장소를 검색하고 결과를 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "장소를 성공적으로 검색했습니다.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청 - 입력 데이터가 유효하지 않습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    )
            }
    )
    ResponseEntity<List<PlatformPlaceSearchResponse>> search(
            String query
    );
}
