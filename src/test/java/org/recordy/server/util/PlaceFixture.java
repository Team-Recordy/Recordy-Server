package org.recordy.server.util;

import org.recordy.server.location.domain.Location;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.domain.usecase.PlaceCreate;

import java.util.List;

public class PlaceFixture {

    public final static String NAME = "Place";

    public static Place create() {
        return Place.create(new PlaceCreate(NAME, LocationFixture.create()));
    }

    public static Place create(long id) {
        return new Place(
                id,
                NAME,
                List.of(),
                LocationFixture.create(),
                null,
                null
        );
    }

    public static Place create(Location location) {
        return Place.create(new PlaceCreate(NAME, location));
    }

    public static Place create(String name, Location location) {
        return Place.create(new PlaceCreate(name, location));
    }
}
