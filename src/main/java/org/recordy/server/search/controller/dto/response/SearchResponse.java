package org.recordy.server.search.controller.dto.response;

import org.recordy.server.search.domain.Search;
import org.recordy.server.search.domain.SearchType;

public record SearchResponse(
        Long id,
        SearchType type,
        String address,
        String name
) {

    public static SearchResponse from(Search search) {
        return new SearchResponse(
                search.getId(),
                search.type(),
                search.address(),
                search.name()
        );
    }
}
