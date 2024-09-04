package org.recordy.server.util;

import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.location.domain.Location;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.domain.usecase.PlaceCreate;

import java.util.List;

public class PlaceFixture {

    public final static long ID = 1L;
    public final static String NAME = "Place";

    public static Place create() {
        return Place.create(new PlaceCreate(ID, NAME, LocationFixture.create()));
    }

    public static Place create(long id, Location location) {
        return Place.create(new PlaceCreate(id, NAME, location));
    }

    public static Place create(long id, List<Exhibition> exhibitions) {
        return Place.create(new PlaceCreate(id, NAME, LocationFixture.create()), exhibitions);
    }

    public static Place create(long id, String name, Location location) {
        return Place.create(new PlaceCreate(id, name, location));
    }

    public static Place create(long id, Location location, List<Exhibition> exhibitions) {
        return Place.create(new PlaceCreate(id, NAME, location), exhibitions);
    }
}
