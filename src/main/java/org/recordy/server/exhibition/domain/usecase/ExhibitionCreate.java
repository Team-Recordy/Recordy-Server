package org.recordy.server.exhibition.domain.usecase;

import java.time.LocalDate;

public record ExhibitionCreate(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate
) {
}
