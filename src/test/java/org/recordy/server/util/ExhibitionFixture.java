package org.recordy.server.util;

import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.domain.usecase.ExhibitionCreate;
import org.recordy.server.place.domain.Place;

import java.time.LocalDate;

public class ExhibitionFixture {

    public final static String NAME = "전시";
    public final static LocalDate START_DATE = LocalDate.of(2021, 1, 1);
    public final static LocalDate END_DATE = LocalDate.of(2021, 1, 31);
    public final static boolean IS_FREE = false;
    public final static String URL = "http://example.com";
    public final static Place PLACE = PlaceFixture.create();

    public static Exhibition create(long id) {
        return Exhibition.create(new ExhibitionCreate(
                id,
                NAME,
                START_DATE,
                END_DATE,
                IS_FREE,
                URL,
                PLACE
        ));
    }

    public static Exhibition create(long id, String name) {
        return Exhibition.create(new ExhibitionCreate(
                id,
                name,
                START_DATE,
                END_DATE,
                IS_FREE,
                URL,
                PLACE
        ));
    }

    public static Exhibition create(long id, Place place) {
        return Exhibition.create(new ExhibitionCreate(
                id,
                NAME,
                START_DATE,
                END_DATE,
                IS_FREE,
                URL,
                place
        ));
    }

    public static Exhibition create(long id, String name, Place place) {
        return Exhibition.create(new ExhibitionCreate(
                id,
                name,
                START_DATE,
                END_DATE,
                IS_FREE,
                URL,
                place
        ));
    }
}
