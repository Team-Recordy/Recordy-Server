package org.recordy.server.util;

import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.location.domain.Location;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.domain.usecase.PlaceCreate;

import java.util.List;

public class PlaceFixture {

    public final static long ID = 1L;
    public final static String NAME = "Place";
    public final static Location LOCATION = LocationFixture.create();

    public static Place create() {
        return Place.create(new PlaceCreate(ID, NAME, LOCATION));
    }

    public static Place create(long id) {
        return Place.create(new PlaceCreate(id, NAME, LOCATION));
    }

    public static Place create(long id, List<Exhibition> exhibitions) {
        return Place.create(new PlaceCreate(id, NAME, LOCATION), exhibitions);
    }
}
