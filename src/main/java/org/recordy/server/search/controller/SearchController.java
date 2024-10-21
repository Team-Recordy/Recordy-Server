package org.recordy.server.search.controller;

import lombok.RequiredArgsConstructor;
import org.recordy.server.search.controller.dto.response.SearchResponse;
import org.recordy.server.search.repository.SearchRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
@RestController
public class SearchController implements SearchApi {

    private final SearchRepository searchRepository;

    @GetMapping
    public ResponseEntity<List<SearchResponse>> search(
            @RequestParam String query
    ) {
        List<SearchResponse> result = searchRepository.search(query).stream()
                .map(SearchResponse::from)
                .toList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }
}
