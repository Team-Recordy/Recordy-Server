package org.recordy.server.record_stat.domain.usecase;

import org.recordy.server.keyword.domain.Keyword;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record Preference(
        long userId,
        List<List<Object>> preference
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

        return new Preference(userId, transform(normalize(topPreference)));
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

    private static List<List<Object>> transform(Map<Keyword, Long> preference) {
        return preference.entrySet().stream()
                .map(entry -> {
                    List<Object> list = new ArrayList<>();
                    list.add(entry.getKey());
                    list.add(entry.getValue());
                    return list;
                })
                .collect(Collectors.toList());
    }
}
