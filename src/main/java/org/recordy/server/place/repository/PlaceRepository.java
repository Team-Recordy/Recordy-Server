package org.recordy.server.place.repository;

import org.recordy.server.place.domain.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PlaceRepository {

    // command
    Place save(Place place);

    // query
    Place findById(long id);
    Slice<Place> findAllOrderByExhibitionStartDateDesc(Pageable pageable);
}
