package org.recordy.server.keyword.domain;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

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

    public byte[] encode() {
        return name().getBytes(StandardCharsets.UTF_8);
    }

    public static List<Keyword> decode(byte[] utf8Bytes) {
        String[] keywords = new String(utf8Bytes, StandardCharsets.UTF_8).split(",");

        return Arrays.stream(keywords)
                .map(Keyword::valueOf)
                .toList();
    }
}
