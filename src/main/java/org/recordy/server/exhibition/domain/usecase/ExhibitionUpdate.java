package org.recordy.server.exhibition.domain.usecase;

import java.time.LocalDate;

public record ExhibitionUpdate(
        String name,
        LocalDate startDate,
        LocalDate endDate
) {
    // TODO: request에 대한 validation 로직 추가
}
