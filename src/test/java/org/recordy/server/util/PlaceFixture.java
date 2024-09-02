package org.recordy.server.util;

import org.recordy.server.place.domain.Place;
import org.recordy.server.place.domain.usecase.PlaceCreate;

public class PlaceFixture {

    public static Place create(long id) {
        return Place.create(new PlaceCreate(id));
    }
}
