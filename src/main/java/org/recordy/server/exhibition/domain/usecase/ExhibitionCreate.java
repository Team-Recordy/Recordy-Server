package org.recordy.server.exhibition.domain.usecase;

import org.recordy.server.exhibition.controller.dto.request.ExhibitionCreateRequest;

import java.time.LocalDate;

public record ExhibitionCreate(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate
) {

    public static ExhibitionCreate from(ExhibitionCreateRequest request) {
        return new ExhibitionCreate(
                null,
                request.name(),
                request.startDate(),
                request.endDate()
        );
    }
}
