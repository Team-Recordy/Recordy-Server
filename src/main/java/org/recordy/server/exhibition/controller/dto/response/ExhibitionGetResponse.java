package org.recordy.server.exhibition.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.recordy.server.location.controller.dto.response.LocationGetResponse;

import java.time.LocalDate;

public record ExhibitionGetResponse(
        Long id,
        String name,
        LocationGetResponse location,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate startDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate endDate
) {
}
