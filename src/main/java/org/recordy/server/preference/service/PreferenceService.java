package org.recordy.server.preference.service;

import org.recordy.server.preference.domain.Preference;

public interface PreferenceService {

    // query
    public Preference getPreference(long userId);
}

