package org.recordy.server.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class PlaceController {

    @Autowired
    private VenueService venueService;

    // 시도 및 구군을 기준으로 문화공간 API에서 전시회장을 검색하고, Google Places API로 매핑하는 엔드포인트
    @GetMapping("/api/map-venue")
    public ResponseEntity<Map<String, Object>> mapVenueToGoogle(
            @RequestParam String sido,
            @RequestParam String gugun) {

        Map<String, Object> mappedVenue = venueService.mapVenueToGoogle(sido, gugun);
        return ResponseEntity.ok(mappedVenue);
    }
}


