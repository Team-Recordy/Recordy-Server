package org.recordy.server.search.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.recordy.server.common.dto.response.ErrorResponse;
import org.recordy.server.search.controller.dto.response.SearchResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "검색 API")
public interface SearchApi {

    @Operation(
            summary = "장소, 전시 검색 API",
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
                                                                    "id": 1 (장소 id),
                                                                    "type": "PLACE" or "EXHIBITION",
                                                                    "address": 서울시 마포구 독막로 209,
                                                                    "name": "클럽 빵"
                                                                },
                                                                {
                                                                    "id": 1 (장소 id),
                                                                    "type": "PLACE" or "EXHIBITION",
                                                                    "address": 서울시 마포구 독막로 209,
                                                                    "name": "클럽 빵"
                                                                },
                                                                {
                                                                    "id": 1 (장소 id),
                                                                    "type": "PLACE" or "EXHIBITION",
                                                                    "address": 서울시 마포구 독막로 209,
                                                                    "name": "클럽 빵"
                                                                },
                                                                {
                                                                    "id": 1 (장소 id),
                                                                    "type": "PLACE" or "EXHIBITION",
                                                                    "address": 서울시 마포구 독막로 209,
                                                                    "name": "클럽 빵"
                                                                },
                                                                {
                                                                    "id": 1 (장소 id),
                                                                    "type": "PLACE" or "EXHIBITION",
                                                                    "address": 서울시 마포구 독막로 209,
                                                                    "name": "클럽 빵"
                                                                }
                                                            ]
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = SearchResponse.class
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
    ResponseEntity<List<SearchResponse>> search(
            @Parameter(description = "검색어") String query
    );
}
