package org.recordy.server.util;

import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.location.domain.Location;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.domain.usecase.PlaceCreate;

import java.util.List;

public class PlaceFixture {

    public final static String NAME = "Place";

    public static Place create() {
        return Place.create(new PlaceCreate(null, NAME, LocationFixture.create()));
    }

    public static Place create(Location location) {
        return Place.create(new PlaceCreate(null, NAME, location));
    }

    public static Place create(String name, Location location) {
        return Place.create(new PlaceCreate(null, name, location));
    }

    public static Place create(long id, List<Exhibition> exhibitions) {
        return Place.create(new PlaceCreate(id, NAME, LocationFixture.create()), exhibitions);
    }
}
