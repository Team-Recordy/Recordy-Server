package org.recordy.server.place.service;

import org.recordy.server.place.controller.dto.request.PlaceCreateRequest;
import org.recordy.server.place.domain.Place;

public interface PlaceService {

    // command
    Place create(PlaceCreateRequest request);
}
