package org.recordy.server.search.domain;

public record Search(
        Long id,
        SearchType type,
        String address,
        String name
) {

    public String getId() {
        return id + type.name();
    }

    public static String getSearchField() {
        return "name";
    }
}
