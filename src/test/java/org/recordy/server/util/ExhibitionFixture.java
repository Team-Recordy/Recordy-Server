package org.recordy.server.util;

import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.domain.usecase.ExhibitionCreate;
import org.recordy.server.place.domain.Place;

import java.time.LocalDate;

public class ExhibitionFixture {

    public final static String NAME = "전시";
    public final static LocalDate START_DATE = LocalDate.now();
    public final static LocalDate END_DATE = LocalDate.now();
    public final static boolean IS_FREE = false;

    public static Exhibition create(long id) {
        return Exhibition.create(new ExhibitionCreate(
                id,
                NAME,
                START_DATE,
                END_DATE,
                IS_FREE,
                PlaceFixture.create()
        ));
    }

    public static Exhibition create(Place place) {
        return Exhibition.create(new ExhibitionCreate(
                null,
                NAME,
                START_DATE,
                END_DATE,
                IS_FREE,
                place
        ));
    }

    public static Exhibition create(String name, Place place) {
        return Exhibition.create(new ExhibitionCreate(
                null,
                name,
                START_DATE,
                END_DATE,
                IS_FREE,
                place
        ));
    }

    public static Exhibition create(LocalDate startDate, LocalDate endDate, Place place) {
        return Exhibition.create(new ExhibitionCreate(
                null,
                NAME,
                startDate,
                endDate,
                IS_FREE,
                place
        ));
    }

    public static Exhibition create(LocalDate startDate, LocalDate endDate, boolean isFree, Place place) {
        return Exhibition.create(new ExhibitionCreate(
                null,
                NAME,
                startDate,
                endDate,
                isFree,
                place
        ));
    }
}
