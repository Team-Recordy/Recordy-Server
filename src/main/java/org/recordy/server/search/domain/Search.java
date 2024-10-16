package org.recordy.server.search.domain;

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
}
