package org.recordy.server.record_stat.domain.usecase;

import org.recordy.server.keyword.domain.Keyword;

import java.security.Key;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public record Preference(
        long userId,
        Map<Keyword, Long> preference
) {

    public static Preference of(long userId, Map<Keyword, Long> preference) {
        Map<Keyword, Long> topPreference = preference.entrySet().stream()
                .sorted(Map.Entry.<Keyword, Long>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new));

        return new Preference(userId, normalize(topPreference));
    }

    private static Map<Keyword, Long> normalize(Map<Keyword, Long> preference) {
        long sum = sum(preference);

        return preference.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue() * 100 / sum,
                        (a, b) -> a,
                        LinkedHashMap::new));
    }

    private static long sum(Map<Keyword, Long> preference) {
        return preference.values().stream()
                .mapToLong(Long::longValue)
                .sum();
    }
}
