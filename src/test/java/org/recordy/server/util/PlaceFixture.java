package org.recordy.server.util;

import org.recordy.server.location.domain.Location;
import org.recordy.server.place.controller.dto.request.PlaceCreateRequest;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.domain.usecase.PlaceCreate;

import java.util.List;

public class PlaceFixture {

    public final static String NAME = "Place";
    public final static String ADDRESS = "서울특별시 마포구 독막로 209";
    public final static String PLATFORM_PLACE_ID = "ChIJcYFQJj2ifDUR4Pn5Z1J6J1A";
    public final static PlaceCreateRequest createRequest = new PlaceCreateRequest(
            PLATFORM_PLACE_ID,
            NAME,
            LocationFixture.POINT.getX(),
            LocationFixture.POINT.getY(),
            ADDRESS
    );

    public static Place create() {
        return Place.create(PlaceCreate.from(createRequest, LocationFixture.create()));
    }

    public static Place create(long id) {
        return new Place(
                id,
                NAME,
                PLATFORM_PLACE_ID,
                ADDRESS,
                List.of(),
                LocationFixture.create(),
                null,
                null
        );
    }

    public static Place create(Location location) {
        return Place.create(PlaceCreate.from(createRequest, location));
    }

    public static Place create(String name, Location location) {
        return Place.create(PlaceCreate.from(createRequest, location));
    }
}
