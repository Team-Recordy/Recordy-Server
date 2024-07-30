package org.recordy.server.preference.domain.usecase;

import org.recordy.server.keyword.domain.Keyword;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record Preference(
        List<List<String>> preference
) {

    public static Preference of(Map<Keyword, Long> preference) {
        Map<Keyword, Long> topPreference = preference.entrySet().stream()
                .sorted(Map.Entry.<Keyword, Long>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new));

        return new Preference(transform(normalize(topPreference, sum(preference))));
    }

    private static Map<Keyword, Long> normalize(Map<Keyword, Long> preference, Long sum) {

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

    private static List<List<String>> transform(Map<Keyword, Long> preference) {
        return preference.entrySet().stream()
                .map(entry -> {
                    List<String> list = new ArrayList<>();
                    list.add(entry.getKey().name());
                    list.add(String.valueOf(entry.getValue()));
                    return list;
                })
                .collect(Collectors.toList());
    }
}