package org.recordy.server.keyword.domain;

public enum Keyword {

    EXOTIC("이색적인"),
    QUITE("조용한"),
    TRENDY("트렌디한"),
    SENSUAL("감각적인"),
    CUTE("귀여운"),
    DUCKUMORI("덕후몰이"),
    FUNNY("재밌는"),
    COZY("아늑한"),
    CLASSIC("클래식한"),
    GOOD_TO_FOCUS("집중하기 좋은"),
    CLEAN("깔끔한"),
    SCARY("무서운"),
    VIBRANT("강렬한"),
    ;

    private final String name;

    Keyword(String name) {
        this.name = name;
    }

    public static Keyword fromString(String keyword) {
        return Keyword.valueOf(keyword.toUpperCase());
    }
}
