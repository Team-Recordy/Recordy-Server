package org.recordy.server.record_stat.domain.usecase;

import org.recordy.server.keyword.domain.Keyword;

import java.util.Map;

public record Preference(
        long userId,
        Map<Keyword, Double> preference
) {
}
