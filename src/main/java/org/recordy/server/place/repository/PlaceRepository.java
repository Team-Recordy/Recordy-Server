package org.recordy.server.place.repository;

import org.locationtech.jts.geom.Point;
import org.recordy.server.place.domain.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PlaceRepository {

    // command
    Place save(Place place);

    // query
    Place findById(long id);
    Slice<Place> findAllOrderByExhibitionStartDateDesc(Pageable pageable);
    Slice<Place> findAllFreeOrderByExhibitionStartDateDesc(Pageable pageable);
    Slice<Place> findAllByNameOrderByExhibitionStartDateDesc(Pageable pageable, String query);
    Slice<Place> findAllByLocationOrderByExhibitionStartDateDesc(Pageable pageable, Point currentLocation, double distance);
}
