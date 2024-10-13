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
    public ResponseEntity<OffsetBasePaginatedResponse<PlaceGetResponse>> getPlacesByExhibitionStartDate(
            @RequestParam(required = false, defaultValue = "0") int number,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Slice<PlaceGetResponse> result = placeService.getPlacesByExhibitionStartDate(PageRequest.of(number, size));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(OffsetBasePaginatedResponse.of(result));
    }

    @Override
    @GetMapping("/exhibitions/geography")
    public ResponseEntity<OffsetBasePaginatedResponse<PlaceGetResponse>> getPlacesByGeography(
            @RequestParam(required = false, defaultValue = "0") int number,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(required = false, defaultValue = "4.0") double distance
    ) {
        Slice<PlaceGetResponse> result = placeService.getPlacesByGeography(PageRequest.of(number, size), latitude, longitude, distance);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(OffsetBasePaginatedResponse.of(result));
    }

    @Override
    @PostMapping
    public ResponseEntity<Place> createPlace(@RequestBody PlaceCreateRequest request) {
        Place createdPlace = placeService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdPlace);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable long id) {
        Place place = placeService.getPlaceById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(place);
    }

    @GetMapping("/name")
    public ResponseEntity<Place> getPlaceByName(@RequestParam String name) {
        Place place = placeService.getPlaceByName(name);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(place);
    }

    @GetMapping("/free")
    public ResponseEntity<Slice<PlaceGetResponse>> getFreePlaces(Pageable pageable) {
        Slice<PlaceGetResponse> places = placeService.getFreePlaces(pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(places);
    }
}
