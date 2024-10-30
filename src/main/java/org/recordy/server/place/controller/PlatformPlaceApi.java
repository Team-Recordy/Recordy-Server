package org.recordy.server.place.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.recordy.server.common.dto.response.ErrorResponse;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.place.controller.dto.response.PlatformPlaceSearchResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "지도 API")
public interface PlatformPlaceApi {

    @Operation(
            summary = "장소 검색 API",
            description = "지도 플랫폼을 통해 장소를 검색하고 결과를 반환합니다.",
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
                                                                    "platformPlaceId": "aldkjf3jbf",
                                                                    "address": "서울시 마포구 독막로 209",
                                                                    "longitude": 100.435,
                                                                    "latitude": 34.55,
                                                                    "name": "초록불꽃소년단의 복귀 공연"
                                                                },
                                                                {
                                                                    "platformPlaceId": "aldkjf3jbf",
                                                                    "address": "서울시 마포구 독막로 209",
                                                                    "longitude": 100.435,
                                                                    "latitude": 34.55,
                                                                    "name": "초록불꽃소년단의 복귀 공연"
                                                                },
                                                                {
                                                                    "platformPlaceId": "aldkjf3jbf",
                                                                    "address": "서울시 마포구 독막로 209",
                                                                    "longitude": 100.435,
                                                                    "latitude": 34.55,
                                                                    "name": "초록불꽃소년단의 복귀 공연"
                                                                },
                                                                {
                                                                    "platformPlaceId": "aldkjf3jbf",
                                                                    "address": "서울시 마포구 독막로 209",
                                                                    "longitude": 100.435,
                                                                    "latitude": 34.55,
                                                                    "name": "초록불꽃소년단의 복귀 공연"
                                                                },
                                                                {
                                                                    "platformPlaceId": "aldkjf3jbf",
                                                                    "address": "서울시 마포구 독막로 209",
                                                                    "longitude": 100.435,
                                                                    "latitude": 34.55,
                                                                    "name": "초록불꽃소년단의 복귀 공연"
                                                                }
                                                            ]
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = PlatformPlaceSearchResponse.class
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
    ResponseEntity<List<PlatformPlaceSearchResponse>> search(
            String query,
            int page
    );
}
