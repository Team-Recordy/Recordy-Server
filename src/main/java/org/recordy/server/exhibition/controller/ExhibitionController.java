package org.recordy.server.exhibition.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.recordy.server.exhibition.controller.dto.request.ExhibitionCreateRequest;
import org.recordy.server.exhibition.controller.dto.request.ExhibitionUpdateRequest;
import org.recordy.server.exhibition.controller.dto.response.ExhibitionGetResponse;
import org.recordy.server.exhibition.domain.usecase.ExhibitionUpdate;
import org.recordy.server.exhibition.service.ExhibitionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/exhibitions")
@RestController
public class ExhibitionController implements ExhibitionApi {

    private final ExhibitionService exhibitionService;

    @Override
    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody @Valid ExhibitionCreateRequest request
    ) {
        exhibitionService.create(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @PatchMapping
    public ResponseEntity<Void> update(
            @RequestBody ExhibitionUpdateRequest request
    ) {
        exhibitionService.update(ExhibitionUpdate.from(request));

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @DeleteMapping("/{exhibitionId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long exhibitionId
    ) {
        exhibitionService.delete(exhibitionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ExhibitionGetResponse>> getAllByPlace(
            @RequestParam Long placeId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(exhibitionService.getAllByPlaceId(placeId));
    }

    @Override
    @GetMapping("/free")
    public ResponseEntity<List<ExhibitionGetResponse>> getAllFreeByPlace(
            @RequestParam Long placeId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(exhibitionService.getAllFreeByPlaceId(placeId));
    }

    @Override
    @GetMapping("/closing")
    public ResponseEntity<List<ExhibitionGetResponse>> getAllClosingByPlace(
            @RequestParam Long placeId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(exhibitionService.getAllByPlaceIdOrderByEndDateDesc(placeId));
    }
}
