package org.recordy.server.util;

import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.exhibition.domain.usecase.ExhibitionCreate;

public class ExhibitionFixture {

    public static Exhibition create(long id) {
        return Exhibition.create(new ExhibitionCreate(id));
    }
}
