package org.recordy.server.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.recordy.server.location.domain.Location;

public class LocationFixture {

    public final static Point POINT = new GeometryFactory().createPoint(new Coordinate(0, 0));
    public final static String ADDRESS = "서울특별시 마포구 독막로 209";
    public final static String GOOGLE_PLACE_ID = "ChIJcYFQJj2ifDUR4Pn5Z1J6J1A";

    public static Location create() {
        return Location.create(null, POINT, ADDRESS, GOOGLE_PLACE_ID, null, null);
    }

    public static Location create(long id) {
        return Location.create(id, POINT, ADDRESS, GOOGLE_PLACE_ID, null, null);
    }

    public static Location create(Point point) {
        return Location.create(null, point, ADDRESS, GOOGLE_PLACE_ID, null, null);
    }
}
