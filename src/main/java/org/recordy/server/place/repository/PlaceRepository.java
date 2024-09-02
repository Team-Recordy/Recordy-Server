package org.recordy.server.place.repository;

import org.recordy.server.place.domain.Place;

public interface PlaceRepository {

    // command
    Place save(Place place);

    // query
    Place findById(long id);
}
