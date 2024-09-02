package org.recordy.server.util;

import org.recordy.server.location.domain.Location;

public class LocationFixture {

    public final static long ID = 1L;

    public static Location create() {
        return new Location(ID, null, null);
    }

    public static Location create(long id) {
        return new Location(id, null, null);
    }
}
