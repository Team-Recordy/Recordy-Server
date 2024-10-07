package org.recordy.server.exhibition.domain.usecase;

import org.recordy.server.exhibition.controller.dto.request.ExhibitionCreateRequest;
import org.recordy.server.place.domain.Place;

import java.time.LocalDate;

public record ExhibitionCreate(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        boolean isFree,
        Place place
) {

    public static ExhibitionCreate of(ExhibitionCreateRequest request, Place place) {
        return new ExhibitionCreate(
                null,
                request.name(),
                request.startDate(),
                request.endDate(),
                request.isFree(),
                place
        );
    }
}
