package org.recordy.server.search.domain;

import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.place.domain.Place;

public record Search(
        Long id,
        SearchType type,
        String address,
        String name
) {

    public static String getSearchField() {
        return "name";
    }

    public static Search from(Exhibition exhibition, Place place) {
        return new Search(
                place.getId(),
                SearchType.EXHIBITION,
                place.getAddress(),
                exhibition.getName()
        );
    }

    public static Search from(Place place) {
        return new Search(
                place.getId(),
                SearchType.PLACE,
                place.getAddress(),
                place.getName()
        );
    }
}
