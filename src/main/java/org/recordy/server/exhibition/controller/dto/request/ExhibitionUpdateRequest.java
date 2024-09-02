package org.recordy.server.exhibition.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record ExhibitionUpdateRequest(
        Long id,
        String name,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate startDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate endDate,
        boolean isFree,
        String url
) {
}
