package org.recordy.server.place.service;

import org.recordy.server.place.controller.dto.request.PlaceCreateRequest;
import org.recordy.server.place.controller.dto.response.PlaceGetResponse;
import org.recordy.server.place.domain.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PlaceService {

    // command
    Place create(PlaceCreateRequest request);

    // query
    Place getPlaceById(long id);
    Place getPlaceByName(String name);
    Slice<PlaceGetResponse> getAllPlaces(Pageable pageable);

    Slice<PlaceGetResponse> getFreePlaces(Pageable pageable);
}
