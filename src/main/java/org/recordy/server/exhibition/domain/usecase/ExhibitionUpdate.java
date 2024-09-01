package org.recordy.server.exhibition.domain.usecase;

import java.time.LocalDate;

public record ExhibitionUpdate(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate
) {
    // TODO: request에 대한 validation 로직 추가
}
