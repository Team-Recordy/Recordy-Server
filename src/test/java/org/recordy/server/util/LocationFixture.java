package org.recordy.server.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.recordy.server.location.domain.Address;
import org.recordy.server.location.domain.Location;

public class LocationFixture {

    public final static Point POINT = new GeometryFactory().createPoint(new Coordinate(0, 0));
    public final static String FORMATTED = "서울특별시 마포구 독막로 209";
    public final static String SIDO = "서울특별시";
    public final static String GUGUN = "마포구";
    public final static String GOOGLE_PLACE_ID = "ChIJcYFQJj2ifDUR4Pn5Z1J6J1A";

    public static Location create() {
        return Location.create(null, POINT, Address.of(FORMATTED, SIDO, GUGUN), GOOGLE_PLACE_ID, null, null);
    }

    public static Location create(long id) {
        return Location.create(id, POINT, Address.of(FORMATTED, SIDO, GUGUN), GOOGLE_PLACE_ID, null, null);
    }

    public static Location create(Point point) {
        return Location.create(null, point, Address.of(FORMATTED, SIDO, GUGUN), GOOGLE_PLACE_ID, null, null);
    }
}
