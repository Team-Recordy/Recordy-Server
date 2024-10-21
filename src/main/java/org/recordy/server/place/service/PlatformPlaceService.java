package org.recordy.server.place.service;

import org.recordy.server.place.controller.dto.response.PlatformPlaceSearchResponse;

import java.util.List;

public interface PlatformPlaceService {

    List<PlatformPlaceSearchResponse> search(String query);
    String searchId(String query);
}
