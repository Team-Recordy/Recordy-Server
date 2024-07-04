package org.recordy.server.keyword.domain;

public enum Keyword {

    EXOTIC(1),
    QUITE(2),
    TRENDY(3),
    SENSUAL(4),
    CUTE(5),
    DUCKUMORI(6),
    FUNNY(7),
    COZY(8),
    CLASSIC(9),
    GOOD_TO_FOCUS(10),
    CLEAN(11),
    SCARY(12),
    VIBRANT(13),
    ;

    private final int id;

    Keyword(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
