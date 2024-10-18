package org.recordy.server.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.recordy.server.location.domain.Location;

public class LocationFixture {

    public final static Point POINT = new GeometryFactory().createPoint(new Coordinate(0, 0));

    public static Location create() {
        return Location.of(POINT);
    }

    public static Location create(long id) {
        return Location.create(id, POINT, null, null);
    }

    public static Location create(Point point) {
        return Location.of(point);
    }
}
