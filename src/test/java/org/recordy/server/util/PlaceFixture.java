package org.recordy.server.util;

import org.recordy.server.location.domain.Location;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.domain.usecase.PlaceCreate;

public class PlaceFixture {

    public final static String NAME = "Place";
    public final static String WEBSITE_URL = "https://www.google.com";

    public static Place create() {
        return Place.create(new PlaceCreate(NAME, WEBSITE_URL, LocationFixture.create()));
    }

    public static Place create(Location location) {
        return Place.create(new PlaceCreate(NAME, WEBSITE_URL, location));
    }

    public static Place create(String name, Location location) {
        return Place.create(new PlaceCreate(name, WEBSITE_URL, location));
    }
}
