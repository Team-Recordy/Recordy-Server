package org.recordy.server.place.repository;

import org.locationtech.jts.geom.Point;
import org.recordy.server.place.controller.dto.response.PlaceGetResponse;
import org.recordy.server.place.domain.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface PlaceRepository {

    // command
    Place save(Place place);
    void cache(Place place);

    // query
    boolean existsByPlatformId(String platformId);
    Place findById(long id);
    Place findByName(String name);
    List<Long> findAllIdsHavingRecords();
    List<Place> findAll();
    PlaceGetResponse findDetailById(Long id);
    List<PlaceGetResponse> findAllByIds(Pageable pageable, List<Long> ids);
    Slice<PlaceGetResponse> findAllOrderByExhibitionStartDateDesc(Pageable pageable);
    Slice<PlaceGetResponse> findAllByLocationOrderByExhibitionStartDateDesc(Pageable pageable, Point currentLocation, double distance);
}
