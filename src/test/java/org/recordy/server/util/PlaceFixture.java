package org.recordy.server.util;

import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.domain.usecase.PlaceCreate;

import java.util.List;

public class PlaceFixture {

    public final static long ID = 1L;

    public static Place create() {
        return Place.create(new PlaceCreate(ID));
    }

    public static Place create(long id) {
        return Place.create(new PlaceCreate(id));
    }

    public static Place create(long id, List<Exhibition> exhibitions) {
        return Place.create(new PlaceCreate(id), exhibitions);
    }
}
