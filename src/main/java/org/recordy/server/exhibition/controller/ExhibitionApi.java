package org.recordy.server.exhibition.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.exhibition.controller.dto.request.ExhibitionCreateRequest;
import org.recordy.server.exhibition.controller.dto.request.ExhibitionUpdateRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "전시 관련 API")
public interface ExhibitionApi {

    @Operation(
            summary = "전시 생성 API",
            description = "전시를 생성하는 API입니다. 전시 이름, 시작일, 종료일을 입력받아 전시를 생성합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "전시가 성공적으로 생성되었습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "request 값이 잘못되었습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "인증이 필요합니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    )
            }
    )
    ResponseEntity<Void> create(
            @RequestBody @Valid ExhibitionCreateRequest request
    );

    @Operation(
            summary = "전시 수정 API",
            description = "전시를 수정하는 API입니다. 수정할 전시의 id와 바뀐 전시 이름, 시작일, 종료일을 입력받아 전시를 수정합니다. (값이 없을 경우 기존 값 유지)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "전시가 성공적으로 수정되었습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "인증이 필요합니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    )
            }
    )
    ResponseEntity<Void> update(
            @RequestBody ExhibitionUpdateRequest request
    );

    @Operation(
            summary = "전시 삭제 API",
            description = "전시를 삭제하는 API입니다. 삭제할 전시의 id를 URI 경로를 통해 입력받아 전시를 삭제합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "전시가 성공적으로 삭제되었습니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "인증이 필요합니다.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorMessage.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
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
            Long exhibitionId
    );
}
