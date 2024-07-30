package org.recordy.server.preference.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.recordy.server.keyword.domain.Keyword;

import java.util.LinkedHashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public class Preference {

    Map<Keyword, Long> keywordAndCounts;
    long sum;

    public static Preference from(Map<Keyword, Long> keywordAndCounts) {
        return new Preference(keywordAndCounts, sum(keywordAndCounts));
    }

    private static long sum(Map<Keyword, Long> preference) {
        return preference.values().stream()
                .mapToLong(Long::longValue)
                .sum();
    }

    public Map<Keyword, Long> getNormalizedTopKeywords(int size) {
        return keywordAndCounts.entrySet().stream()
                .sorted(Map.Entry.<Keyword, Long>comparingByValue().reversed())
                .limit(size)
                .collect(LinkedHashMap::new,
                        (map, entry) -> map.put(entry.getKey(), normalize(entry.getValue(), sum)),
                        Map::putAll);
    }

    private static long normalize(long value, long sum) {
        return value * 100 / sum;
    }
}