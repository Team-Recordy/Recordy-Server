package org.recordy.server.mock.place;

import org.locationtech.jts.geom.Point;
import org.recordy.server.place.controller.dto.response.PlaceGetResponse;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.repository.PlaceRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public class FakePlaceRepository implements PlaceRepository {

    @Override
    public Place save(Place place) {
        return null;
    }

    @Override
    public Place findById(long id) {
        return null;
    }

    @Override
    public Slice<PlaceGetResponse> findAllOrderByExhibitionStartDateDesc(Pageable pageable) {
        return null;
    }

    @Override
    public Slice<PlaceGetResponse> findAllFreeOrderByExhibitionStartDateDesc(Pageable pageable) {
        return null;
    }

    @Override
    public Slice<PlaceGetResponse> findAllByNameOrderByExhibitionStartDateDesc(Pageable pageable, String query) {
        return null;
    }

    @Override
    public Slice<PlaceGetResponse> findAllByLocationOrderByExhibitionStartDateDesc(Pageable pageable, Point currentLocation, double distance) {
        return null;
    }
}
