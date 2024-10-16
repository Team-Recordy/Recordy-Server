package org.recordy.server.search.domain;

import lombok.Getter;

@Getter
public enum SearchType {

    PLACE("place"),
    EXHIBITION("exhibition"),
    ;

    private final String name;

    SearchType(String name) {
        this.name = name;
    }
}
