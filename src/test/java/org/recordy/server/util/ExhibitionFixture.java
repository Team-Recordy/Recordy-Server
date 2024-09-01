package org.recordy.server.util;

import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.domain.usecase.ExhibitionCreate;

import java.time.LocalDate;

public class ExhibitionFixture {

    public final static String NAME = "전시";
    public final static LocalDate START_DATE = LocalDate.of(2021, 1, 1);
    public final static LocalDate END_DATE = LocalDate.of(2021, 1, 31);

    public static Exhibition create(long id) {
        return Exhibition.create(new ExhibitionCreate(
                id,
                NAME,
                START_DATE,
                END_DATE
        ));
    }

    public static Exhibition create(long id, String name) {
        return Exhibition.create(new ExhibitionCreate(
                id,
                name,
                START_DATE,
                END_DATE
        ));
    }
}
