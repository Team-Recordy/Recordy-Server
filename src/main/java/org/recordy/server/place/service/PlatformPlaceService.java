package org.recordy.server.place.service;

import org.recordy.server.place.domain.usecase.PlatformPlace;
import org.recordy.server.place.service.dto.PlatformPlaceSearchResponse;

import java.util.List;

public interface PlatformPlaceService {

    PlatformPlace getById(String id);
    String searchId(String query);
    List<PlatformPlaceSearchResponse> searchAll(String query);
}
