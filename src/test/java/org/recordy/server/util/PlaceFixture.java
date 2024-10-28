package org.recordy.server.util;

import org.recordy.server.location.domain.Location;
import org.recordy.server.place.controller.dto.request.PlaceCreateRequest;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.domain.usecase.PlaceCreate;

import java.util.UUID;

public class PlaceFixture {

    public final static String NAME = "Place";
    public final static String ADDRESS = "서울특별시 마포구 독막로 209";
    public final static PlaceCreateRequest createRequest = new PlaceCreateRequest(
            UUID.randomUUID().toString(),
            NAME,
            LocationFixture.POINT.getX(),
            LocationFixture.POINT.getY(),
            ADDRESS
    );

    public static Place create(long id) {
        return new Place(
                id,
                NAME,
                UUID.randomUUID().toString(),
                ADDRESS,
                LocationFixture.create(),
                null,
                null
        );
    }

    public static Place create() {
        return Place.create(PlaceCreate.from(placeCreateRequest(), LocationFixture.create()));
    }

    public static Place create(Location location) {
        return Place.create(PlaceCreate.from(placeCreateRequest(), location));
    }

    private static PlaceCreateRequest placeCreateRequest() {
        return new PlaceCreateRequest(
                UUID.randomUUID().toString(),
                NAME,
                LocationFixture.POINT.getX(),
                LocationFixture.POINT.getY(),
                ADDRESS
        );
    }
}
