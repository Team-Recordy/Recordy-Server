package org.recordy.server.keyword.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum Keyword {

    감각적인(1),
    강렬한(2),
    귀여운(3),
    깔끔한(4),
    덕후몰이(5),
    아늑한(6),
    이색적인(7),
    재밌는(8),
    조용한(9),
    집중하기_좋은(10),
    클래식한(11),
    트렌디한(12),
    ;

    private final long id;

    public static List<Keyword> decode(String utf8Bytes) {
        String[] keywords = new String(Base64.getDecoder().decode(utf8Bytes), StandardCharsets.UTF_8).split(",");

        return Arrays.stream(keywords)
                .map(Keyword::valueOf)
                .collect(Collectors.toList());
    }
}
