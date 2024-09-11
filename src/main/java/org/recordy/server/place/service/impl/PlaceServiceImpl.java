package org.recordy.server.place.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.place.controller.dto.request.PlaceCreateRequest;
import org.recordy.server.place.repository.PlaceRepository;
import org.recordy.server.place.service.GooglePlaceService;
import org.recordy.server.place.service.PlaceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PlaceServiceImpl implements PlaceService {

    private PlaceRepository placeRepository;
    private GooglePlaceService googlePlaceService;

    @Override
    public void create(PlaceCreateRequest request) {
    }
}
