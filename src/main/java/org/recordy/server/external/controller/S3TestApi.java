package org.recordy.server.external.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "S3 테스트 관련 API")
public interface S3TestApi {

    @Operation(
            summary = "이미지 업로드 테스트 API",
            description = "S3에 이미지가 업로드 되는지 테스트하는 API입니다. 이미지 사이즈는 5MB를 넘을 수 없습니다. 이미지 확장자는 jpg, png, webp만 가능합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공",
                            content = @Content(
                                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE
                            )
                    )
            }
    )
    public String uploadTest(@RequestPart MultipartFile file) throws IOException;
}
