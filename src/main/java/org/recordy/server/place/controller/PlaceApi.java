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
import org.recordy.server.place.controller.dto.response.PlaceReviewGetResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

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
    ResponseEntity<Void> createPlace(PlaceCreateRequest request);

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
    ResponseEntity<OffsetBasePaginatedResponse<PlaceGetResponse>> getAllByExhibitionStartDate(
            int number,
            int size
    );

    @Operation(
            summary = "현재 진행중인 전시를 가지며 거리 범위에 포함되는 장소 리스트 조회 API",
            description = "현재 진행중인 전시를 가지며 거리 범위에 포함되는 장소의 리스트를, 거리가 가까운 장소 순서대로 조회합니다.",
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
    ResponseEntity<OffsetBasePaginatedResponse<PlaceGetResponse>> getAllByGeography(
            int number,
            int size,
            double latitude,
            double longitude,
            double distance
    );

    @Operation(
            summary = "id 기반 장소 조회 API",
            description = "동일한 id를 가진 장소를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "장소를 성공적으로 조회했습니다.",
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
                            responseCode = "404",
                            description = "잘못된 요청 - 동일한 id를 가진 장소가 없습니다.",
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
    ResponseEntity<PlaceGetResponse> getById(
            Long id
    );

    @Operation(
            summary = "id 기반 장소 리뷰 리스트 조회 API",
            description = "장소와 관련된 리뷰 리스트를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "장소 리뷰 리스트를 성공적으로 조회했습니다.",
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
    ResponseEntity<List<PlaceReviewGetResponse>> getReviewsById(
            Long id
    );
}
