package org.recordy.server.place.controller;

import lombok.RequiredArgsConstructor;
import org.recordy.server.place.service.PlatformPlaceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/places/platform")
@RestController
public class PlatformPlaceController {

    private final PlatformPlaceService platformPlaceService;
}
