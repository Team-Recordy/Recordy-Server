package org.recordy.server.preference.controller.dto.response;

import org.recordy.server.preference.domain.Preference;

import java.util.List;

public record PreferenceGetResponse(
        List<List<String>> preference
) {

    public static PreferenceGetResponse from(Preference preference) {
        return new PreferenceGetResponse(
                preference.getNormalizedTopKeywords(3)
                        .entrySet().stream()
                        .map(entry ->
                                List.of(entry.getKey().name(), String.valueOf(entry.getValue()))
                        )
                        .toList()
        );
    }
}
