package org.recordy.server.place.service;

import org.recordy.server.place.domain.usecase.PlaceGoogle;

public interface GooglePlaceService {

    PlaceGoogle search(String query);
}
