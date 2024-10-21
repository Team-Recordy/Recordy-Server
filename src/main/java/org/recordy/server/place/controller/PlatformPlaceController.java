package org.recordy.server.place.controller;

import lombok.RequiredArgsConstructor;
import org.recordy.server.place.service.PlatformPlaceService;
import org.recordy.server.place.controller.dto.response.PlatformPlaceSearchResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/places/platform")
@RestController
public class PlatformPlaceController implements PlatformPlaceApi {

    private final PlatformPlaceService platformPlaceService;

    @GetMapping("/search")
    public ResponseEntity<List<PlatformPlaceSearchResponse>> search(
            @RequestParam String query
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(platformPlaceService.search(query));
    }
}
