package org.recordy.server.keyword.domain;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public enum Keyword {

    감각적인,
    강렬한,
    귀여운,
    깔끔한,
    덕후몰이,
    아늑한,
    이색적인,
    재밌는,
    조용한,
    집중하기_좋은,
    클래식한,
    트렌디한,
    ;

    public static String encode() {
        return Base64.getEncoder().encodeToString(
                Arrays.stream(Keyword.values())
                        .map(Enum::name)
                        .reduce((a, b) -> a + "," + b)
                        .orElseThrow()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }

    public static List<Keyword> decode(String utf8Bytes) {
        String[] keywords = new String(Base64.getDecoder().decode(utf8Bytes), StandardCharsets.UTF_8).split(",");

        System.out.println(Arrays.toString(keywords));

        return Arrays.stream(keywords)
                .map(Keyword::valueOf)
                .collect(Collectors.toList());
    }
}
