package org.recordy.server.place.service;

import org.recordy.server.place.controller.dto.request.PlaceCreateRequest;

public interface PlaceService {

    // command
    void create(PlaceCreateRequest request);
}
