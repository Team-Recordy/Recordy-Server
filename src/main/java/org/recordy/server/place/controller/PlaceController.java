package org.recordy.server.place.controller;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.dto.response.OffsetBasePaginatedResponse;
import org.recordy.server.place.controller.dto.request.PlaceCreateRequest;
import org.recordy.server.place.controller.dto.response.PlaceGetResponse;
import org.recordy.server.place.domain.Place;
import org.recordy.server.place.service.PlaceService;
import org.springframework.data.domain.PageRequest;
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
    @GetMapping("/exhibitions/date")
    public ResponseEntity<OffsetBasePaginatedResponse<PlaceGetResponse>> getAllByExhibitionStartDate(
            @RequestParam(required = false, defaultValue = "0") int number,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Slice<PlaceGetResponse> result = placeService.getAllByExhibitionStartDate(PageRequest.of(number, size));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(OffsetBasePaginatedResponse.of(result));
    }

    @Override
    @GetMapping("/exhibitions/geography")
    public ResponseEntity<OffsetBasePaginatedResponse<PlaceGetResponse>> getAllByGeography(
            @RequestParam(required = false, defaultValue = "0") int number,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(required = false, defaultValue = "4.0") double distance
    ) {
        Slice<PlaceGetResponse> result = placeService.getAllByGeography(PageRequest.of(number, size), latitude, longitude, distance);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(OffsetBasePaginatedResponse.of(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceGetResponse> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(placeService.getDetailById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<Place> createPlace(@RequestBody PlaceCreateRequest request) {
        Place createdPlace = placeService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdPlace);
    }

    @GetMapping("/free")
    public ResponseEntity<Slice<PlaceGetResponse>> getFreePlaces(Pageable pageable) {
        Slice<PlaceGetResponse> places = placeService.getFreePlaces(pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(places);
    }
}
