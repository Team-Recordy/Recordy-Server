package org.recordy.server.place.repository;

import org.locationtech.jts.geom.Point;
import org.recordy.server.place.controller.dto.response.PlaceGetResponse;
import org.recordy.server.place.domain.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PlaceRepository {

    // command
    Place save(Place place);

    // query
    Place findById(long id);
    Place findByName(String name);
    PlaceGetResponse findDetailById(Long id);
    Slice<PlaceGetResponse> findAllOrderByExhibitionStartDateDesc(Pageable pageable);
    Slice<PlaceGetResponse> findAllByNameOrderByExhibitionStartDateDesc(Pageable pageable, String query);
    Slice<PlaceGetResponse> findAllByLocationOrderByExhibitionStartDateDesc(Pageable pageable, Point currentLocation, double distance);
}
