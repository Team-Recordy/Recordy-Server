package org.recordy.server.place.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.location.domain.Location;
import org.recordy.server.place.controller.dto.request.PlaceCreateRequest;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.domain.usecase.PlaceCreate;
import org.recordy.server.place.domain.usecase.PlaceGoogle;
import org.recordy.server.place.repository.PlaceRepository;
import org.recordy.server.place.service.GooglePlaceService;
import org.recordy.server.place.service.PlaceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final GooglePlaceService googlePlaceService;

    @Override
    public void create(PlaceCreateRequest request) {
        PlaceGoogle placeGoogle = googlePlaceService.search(request.toQuery());
        Location location = Location.of(placeGoogle, request.sido(), request.gugun());

        placeRepository.save(Place.create(new PlaceCreate(request.name(), location)));
    }
}
