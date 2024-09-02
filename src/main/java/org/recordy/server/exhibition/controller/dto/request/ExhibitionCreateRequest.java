package org.recordy.server.exhibition.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ExhibitionCreateRequest(
        @NotBlank(message = "전시 이름이 입력되지 않았습니다.")
        String name,
        @NotNull(message = "전시 시작일이 입력되지 않았습니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate startDate,
        @NotNull(message = "전시 종료일이 입력되지 않았습니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate endDate,
        @NotNull(message = "전시 유무료 여부가 입력되지 않았습니다.")
        boolean isFree,
        @NotBlank(message = "전시 홈페이지 url이 입력되지 않았습니다.")
        String url
) {
}
