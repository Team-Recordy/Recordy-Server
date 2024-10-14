package org.recordy.server.place.service;

import org.recordy.server.place.controller.dto.request.PlaceCreateRequest;
import org.recordy.server.place.controller.dto.response.PlaceGetResponse;
import org.recordy.server.place.controller.dto.response.PlaceReviewGetResponse;
import org.recordy.server.place.domain.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface PlaceService {

    // command
    Place create(PlaceCreateRequest request);

    // query
    Place getPlaceByName(String name);
    PlaceGetResponse getDetailById(Long id);
    Slice<PlaceGetResponse> getAllByExhibitionStartDate(Pageable pageable);
    Slice<PlaceGetResponse> getAllByGeography(Pageable pageable, double latitude, double longitude, double distance);
    List<PlaceReviewGetResponse> getReviewsByPlaceId(long id);
    Slice<PlaceGetResponse> getFreePlaces(Pageable pageable);
}
