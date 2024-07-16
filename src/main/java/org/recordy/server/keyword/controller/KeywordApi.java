package org.recordy.server.keyword.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.recordy.server.keyword.domain.Keyword;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "키워드 관련 API")
public interface KeywordApi {

    @Operation(
            summary = "키워드 리스트 조회 API",
            description = "사용중인 모든 키워드를 조회하는 API입니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공"
                    )
            }
    )
    ResponseEntity<List<Keyword>> getKeywords();
}
