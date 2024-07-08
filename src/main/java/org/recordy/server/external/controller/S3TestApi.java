package org.recordy.server.external.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.recordy.server.user.controller.dto.response.UserSignInResponse;
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
    public String uploadImageTest(@RequestPart MultipartFile image) throws IOException;

    @Operation(
            summary = "비디오 업로드 테스트 API",
            description = "S3에 비디오가 업로드 되는지 테스트하는 API입니다. 사진이 업로드 되지 않도록 비디오와 이미지를 구분하였습니다. 비디오 사이즈는 100MB를 넘을 수 없습니다. 비디오 확장자는 mp4, mov만 가능합니다.",
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
    public String uploadVideoTest(@RequestPart MultipartFile video) throws IOException;

}
