package org.recordy.server.mock.place;

import org.recordy.server.place.domain.Place;
import org.recordy.server.place.domain.usecase.PlaceCreate;
import org.recordy.server.place.repository.PlaceRepository;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class FakePlaceRepository implements PlaceRepository {

    public long placeAutoIncrementId = 1L;
    private final Map<Long, Place> places = new ConcurrentHashMap<>();

    @Override
    public Place save(Place place) {
        if (Objects.nonNull(place.getId())) {
            places.put(place.getId(), place);

            return place;
        }

        PlaceCreate create = new PlaceCreate(
                placeAutoIncrementId,
                place.getName(),
                place.getLocation()
        );

        Place realPlace = Place.create(create);
        places.put(placeAutoIncrementId++, realPlace);

        return realPlace;
    }

    @Override
    public Place findById(long id) {
        return places.get(id);
    }
}
