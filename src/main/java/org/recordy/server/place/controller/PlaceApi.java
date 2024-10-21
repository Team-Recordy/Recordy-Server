package org.recordy.server.place.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.recordy.server.common.dto.response.ErrorResponse;
import org.recordy.server.common.dto.response.OffsetBasePaginatedResponse;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.place.controller.dto.request.PlaceCreateRequest;
import org.recordy.server.place.controller.dto.response.PlaceCreateResponse;
import org.recordy.server.place.controller.dto.response.PlaceGetResponse;
import org.recordy.server.place.controller.dto.response.PlaceReviewGetResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "공간 API")
public interface PlaceApi {

    @Operation(
            summary = "장소 생성 API",
            description = "새로운 장소를 생성합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "placeId" : 3
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = PlaceCreateResponse.class
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
    ResponseEntity<PlaceCreateResponse> createPlace(
            PlaceCreateRequest request
    );

    @Operation(
            summary = "현재 진행중인 전시를 가지는 장소 리스트 조회 API",
            description = "현재 진행중인 전시를 가지는 장소의 리스트를, 가장 최근 시작한 전시를 가지는 장소 순서대로 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "pageNumber": 3,
                                                                "hasNext": true,
                                                                "content": [
                                                                    {
                                                                        "id": 1,
                                                                        "name": "클럽 빵"
                                                                        "address": "서울시 마포구 독막로 209",
                                                                        "platformId": "alsdkfje2",
                                                                        "location": {
                                                                            "id": 3,
                                                                            "point": {
                                                                                "id": 74,
                                                                                "longitude": 123.12314124,
                                                                                "latitude": 123.12314124
                                                                            }
                                                                        },
                                                                        "exhibitionSize": 5,
                                                                        "recordSize": 109
                                                                    },
                                                                    {
                                                                        "id": 1,
                                                                        "name": "클럽 빵"
                                                                        "address": "서울시 마포구 독막로 209",
                                                                        "platformId": "alsdkfje2",
                                                                        "location": {
                                                                            "id": 3,
                                                                            "point": {
                                                                                "id": 74,
                                                                                "longitude": 123.12314124,
                                                                                "latitude": 123.12314124
                                                                            }
                                                                        },
                                                                        "exhibitionSize": 5,
                                                                        "recordSize": 109
                                                                    },
                                                                    {
                                                                        "id": 1,
                                                                        "name": "클럽 빵"
                                                                        "address": "서울시 마포구 독막로 209",
                                                                        "platformId": "alsdkfje2",
                                                                        "location": {
                                                                            "id": 3,
                                                                            "point": {
                                                                                "id": 74,
                                                                                "longitude": 123.12314124,
                                                                                "latitude": 123.12314124
                                                                            }
                                                                        },
                                                                        "exhibitionSize": 5,
                                                                        "recordSize": 109
                                                                    },
                                                                    {
                                                                        "id": 1,
                                                                        "name": "클럽 빵"
                                                                        "address": "서울시 마포구 독막로 209",
                                                                        "platformId": "alsdkfje2",
                                                                        "location": {
                                                                            "id": 3,
                                                                            "point": {
                                                                                "id": 74,
                                                                                "longitude": 123.12314124,
                                                                                "latitude": 123.12314124
                                                                            }
                                                                        },
                                                                        "exhibitionSize": 5,
                                                                        "recordSize": 109
                                                                    },
                                                                    {
                                                                        "id": 1,
                                                                        "name": "클럽 빵"
                                                                        "address": "서울시 마포구 독막로 209",
                                                                        "platformId": "alsdkfje2",
                                                                        "location": {
                                                                            "id": 3,
                                                                            "point": {
                                                                                "id": 74,
                                                                                "longitude": 123.12314124,
                                                                                "latitude": 123.12314124
                                                                            }
                                                                        },
                                                                        "exhibitionSize": 5,
                                                                        "recordSize": 109
                                                                    }
                                                                ]
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = PlaceGetResponse.class
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
    ResponseEntity<OffsetBasePaginatedResponse<PlaceGetResponse>> getAllByExhibitionStartDate(
            int number,
            int size
    );

    @Operation(
            summary = "거리 안에 포함되는 장소 리스트 조회 API",
            description = "현재 진행중인 전시를 가지며 거리 범위에 포함되는 장소의 리스트를, 거리가 가까운 장소 순서대로 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "pageNumber": 3,
                                                                "hasNext": true,
                                                                "content": [
                                                                    {
                                                                        "id": 1,
                                                                        "name": "클럽 빵"
                                                                        "address": "서울시 마포구 독막로 209",
                                                                        "platformId": "alsdkfje2",
                                                                        "location": {
                                                                            "id": 3,
                                                                            "point": {
                                                                                "id": 74,
                                                                                "longitude": 123.12314124,
                                                                                "latitude": 123.12314124
                                                                            }
                                                                        },
                                                                        "exhibitionSize": 5,
                                                                        "recordSize": 109
                                                                    },
                                                                    {
                                                                        "id": 1,
                                                                        "name": "클럽 빵"
                                                                        "address": "서울시 마포구 독막로 209",
                                                                        "platformId": "alsdkfje2",
                                                                        "location": {
                                                                            "id": 3,
                                                                            "point": {
                                                                                "id": 74,
                                                                                "longitude": 123.12314124,
                                                                                "latitude": 123.12314124
                                                                            }
                                                                        },
                                                                        "exhibitionSize": 5,
                                                                        "recordSize": 109
                                                                    },
                                                                    {
                                                                        "id": 1,
                                                                        "name": "클럽 빵"
                                                                        "address": "서울시 마포구 독막로 209",
                                                                        "platformId": "alsdkfje2",
                                                                        "location": {
                                                                            "id": 3,
                                                                            "point": {
                                                                                "id": 74,
                                                                                "longitude": 123.12314124,
                                                                                "latitude": 123.12314124
                                                                            }
                                                                        },
                                                                        "exhibitionSize": 5,
                                                                        "recordSize": 109
                                                                    },
                                                                    {
                                                                        "id": 1,
                                                                        "name": "클럽 빵"
                                                                        "address": "서울시 마포구 독막로 209",
                                                                        "platformId": "alsdkfje2",
                                                                        "location": {
                                                                            "id": 3,
                                                                            "point": {
                                                                                "id": 74,
                                                                                "longitude": 123.12314124,
                                                                                "latitude": 123.12314124
                                                                            }
                                                                        },
                                                                        "exhibitionSize": 5,
                                                                        "recordSize": 109
                                                                    },
                                                                    {
                                                                        "id": 1,
                                                                        "name": "클럽 빵"
                                                                        "address": "서울시 마포구 독막로 209",
                                                                        "platformId": "alsdkfje2",
                                                                        "location": {
                                                                            "id": 3,
                                                                            "point": {
                                                                                "id": 74,
                                                                                "longitude": 123.12314124,
                                                                                "latitude": 123.12314124
                                                                            }
                                                                        },
                                                                        "exhibitionSize": 5,
                                                                        "recordSize": 109
                                                                    }
                                                                ]
                                                            }
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = PlaceGetResponse.class
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
    ResponseEntity<OffsetBasePaginatedResponse<PlaceGetResponse>> getAllByGeography(
            int number,
            int size,
            double latitude,
            double longitude,
            double distance
    );

    @Operation(
            summary = "장소 단건 조회 API",
            description = "동일한 id를 가진 장소를 조회합니다.",
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
                            responseCode = "404",
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
    ResponseEntity<PlaceGetResponse> getById(
            @Parameter(
                    name = "액세스 토큰",
                    in = ParameterIn.HEADER,
                    required = true,
                    schema = @Schema(
                            implementation = String.class
                    )
            ) Long id
    );

    @Operation(
            summary = "장소 리뷰 리스트 조회 API",
            description = "장소와 관련된 리뷰 리스트를 조회합니다.",
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
                                                                    "authorName": "konu",
                                                                    "content": "코드가 잘생겼고, 사장님이 맛있어요.",
                                                                    "rating": 3,
                                                                    "createdAt": "2024-10-21T15:40:40.123"
                                                                },
                                                                {
                                                                    "id": 1,
                                                                    "authorName": "konu",
                                                                    "content": "코드가 잘생겼고, 사장님이 맛있어요.",
                                                                    "rating": 3,
                                                                    "createdAt": "2024-10-21T15:40:40.123"
                                                                },
                                                                {
                                                                    "id": 1,
                                                                    "authorName": "konu",
                                                                    "content": "코드가 잘생겼고, 사장님이 맛있어요.",
                                                                    "rating": 3,
                                                                    "createdAt": "2024-10-21T15:40:40.123"
                                                                },
                                                                {
                                                                    "id": 1,
                                                                    "authorName": "konu",
                                                                    "content": "코드가 잘생겼고, 사장님이 맛있어요.",
                                                                    "rating": 3,
                                                                    "createdAt": "2024-10-21T15:40:40.123"
                                                                },
                                                                {
                                                                    "id": 1,
                                                                    "authorName": "konu",
                                                                    "content": "코드가 잘생겼고, 사장님이 맛있어요.",
                                                                    "rating": 3,
                                                                    "createdAt": "2024-10-21T15:40:40.123"
                                                                }
                                                            ]
                                                            """
                                            )
                                    },
                                    schema = @Schema(
                                            implementation = PlaceReviewGetResponse.class
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
    ResponseEntity<List<PlaceReviewGetResponse>> getReviewsById(
            @Parameter(
                    name = "액세스 토큰",
                    in = ParameterIn.HEADER,
                    required = true,
                    schema = @Schema(
                            implementation = String.class
                    )
            ) Long id
    );
}
