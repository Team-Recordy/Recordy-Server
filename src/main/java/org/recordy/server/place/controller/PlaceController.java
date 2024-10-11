package org.recordy.server.place.controller;

import lombok.RequiredArgsConstructor;
import org.recordy.server.place.controller.dto.request.PlaceCreateRequest;
import org.recordy.server.place.controller.dto.response.PlaceGetResponse;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.service.PlaceService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
@RestController
public class PlaceController implements PlaceApi {

    private final PlaceService placeService;

    @Override
    @PostMapping
    public ResponseEntity<Place> createPlace(@RequestBody PlaceCreateRequest request) {
        Place createdPlace = placeService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdPlace);
    }


}
