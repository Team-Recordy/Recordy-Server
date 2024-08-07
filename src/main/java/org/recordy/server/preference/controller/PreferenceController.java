package org.recordy.server.preference.controller;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.security.resolver.UserId;
import org.recordy.server.preference.controller.dto.response.PreferenceGetResponse;
import org.recordy.server.preference.service.PreferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/preference")
@RestController
public class PreferenceController implements PreferenceApi {

    private final PreferenceService preferenceService;

    @Override
    @GetMapping
    public ResponseEntity<PreferenceGetResponse> getPreference(
            @UserId Long userId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PreferenceGetResponse.from(preferenceService.getPreference(userId)));
    }
}

