package org.recordy.server.util;

import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.domain.usecase.ExhibitionCreate;

import java.time.LocalDate;

public class ExhibitionFixture {

    private final static String NAME = "전시";
    private final static LocalDate START_DATE = LocalDate.of(2021, 1, 1);
    private final static LocalDate END_DATE = LocalDate.of(2021, 1, 31);

    public static Exhibition create(long id) {
        return Exhibition.create(new ExhibitionCreate(
                id,
                NAME,
                START_DATE,
                END_DATE
        ));
    }
}
