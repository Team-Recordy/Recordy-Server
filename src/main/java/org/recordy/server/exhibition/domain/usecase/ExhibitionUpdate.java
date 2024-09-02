package org.recordy.server.exhibition.domain.usecase;

import org.recordy.server.exhibition.controller.dto.request.ExhibitionUpdateRequest;

import java.time.LocalDate;

public record ExhibitionUpdate(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        boolean isFree,
        String url
) {
    public static ExhibitionUpdate from(ExhibitionUpdateRequest request) {
        return new ExhibitionUpdate(
                request.id(),
                request.name(),
                request.startDate(),
                request.endDate(),
                request.isFree(),
                request.url()
        );
    }
}
