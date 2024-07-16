package org.recordy.server.keyword.controller;

import lombok.RequiredArgsConstructor;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.keyword.service.KeywordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/keywords")
@RestController
public class KeywordController {

    private final KeywordService keywordService;

    @GetMapping
    public ResponseEntity<List<Keyword>> getKeywords() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(keywordService.getAll());
    }
}
