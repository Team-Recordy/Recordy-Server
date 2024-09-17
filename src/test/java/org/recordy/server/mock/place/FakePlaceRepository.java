package org.recordy.server.mock.place;

import org.locationtech.jts.geom.Point;
import org.recordy.server.place.controller.dto.response.PlaceGetResponse;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.repository.PlaceRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FakePlaceRepository implements PlaceRepository {

    public long placeAutoIncrementId = 1L;
    private final Map<Long, Place> places = new ConcurrentHashMap<>();

    @Override
    public Place save(Place place) {
        Place realPlace = new Place(
                placeAutoIncrementId,
                place.getName(),
                place.getWebsiteUrl(),
                place.getExhibitions(),
                place.getLocation(),
                place.getCreatedAt(),
                place.getUpdatedAt()
        );
        places.put(placeAutoIncrementId++, realPlace);

        return realPlace;
    }

    @Override
    public Place findById(long id) {
        return places.get(id);
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
