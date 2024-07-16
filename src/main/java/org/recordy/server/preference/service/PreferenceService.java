package org.recordy.server.preference.service;

import java.util.List;
import org.recordy.server.preference.domain.usecase.Preference;
import org.recordy.server.record.domain.Record;
import org.springframework.data.domain.Slice;

public interface PreferenceService {

    // query
    public Preference getPreference(long userId);
}

