package org.recordy.server.search.domain;

import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.place.domain.Place;

public record Search(
        String id,
        SearchType type,
        String address,
        String name
) {

    public Long getId() {
        return Long.parseLong(id.split(":")[0]);
    }

    public static String getSearchField() {
        return "name";
    }

    public static Search from(Exhibition exhibition, String address) {
        return new Search(
                setId(exhibition.getId(), SearchType.EXHIBITION),
                SearchType.EXHIBITION,
                address,
                exhibition.getName()
        );
    }

    public static Search from(Place place) {
        return new Search(
                setId(place.getId(), SearchType.PLACE),
                SearchType.PLACE,
                place.getLocation().getAddress(),
                place.getName()
        );
    }

    private static String setId(long id, SearchType type) {
        return id + ":" + type.getName();
    }
}
