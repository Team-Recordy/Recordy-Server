package org.recordy.server.place.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.recordy.server.common.dto.response.OffsetBasePaginatedResponse;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.place.controller.dto.request.PlaceCreateRequest;
import org.recordy.server.place.controller.dto.response.PlaceGetResponse;
import org.recordy.server.place.domain.Place;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Place Api")
public interface PlaceApi {
    @Operation(
            summary = "장소 생성 API",
            description = "새로운 장소를 생성합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "장소가 성공적으로 생성되었습니다.",
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
    ResponseEntity<Place> createPlace(@RequestBody PlaceCreateRequest request);

    @Operation(
            summary = "현재 진행중인 전시를 가지는 장소 리스트 조회 API",
            description = "현재 진행중인 전시를 가지는 장소의 리스트를, 가장 최근 시작한 전시를 가지는 장소 순서대로 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "장소 리스트를 성공적으로 조회했습니다.",
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
    ResponseEntity<OffsetBasePaginatedResponse<PlaceGetResponse>> getPlacesByExhibitionStartDate(
            int number,
            int size
    );
}
