package org.recordy.server.exhibition.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.recordy.server.exhibition.controller.dto.request.ExhibitionCreateRequest;
import org.recordy.server.exhibition.controller.dto.request.ExhibitionUpdateRequest;
import org.recordy.server.exhibition.domain.usecase.ExhibitionCreate;
import org.recordy.server.exhibition.domain.usecase.ExhibitionUpdate;
import org.recordy.server.exhibition.service.ExhibitionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        exhibitionService.create(ExhibitionCreate.from(request));

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
}
