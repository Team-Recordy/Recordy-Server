package org.recordy.server.util;

import org.recordy.server.place.domain.Place;
import org.recordy.server.place.domain.usecase.PlaceCreate;

public class PlaceFixture {

    public final static long ID = 1L;

    public static Place create() {
        return Place.create(new PlaceCreate(ID));
    }

    public static Place create(long id) {
        return Place.create(new PlaceCreate(id));
    }
}
