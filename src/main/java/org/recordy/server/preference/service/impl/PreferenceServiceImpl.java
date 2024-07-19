package org.recordy.server.preference.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.preference.domain.usecase.Preference;
import org.recordy.server.preference.service.PreferenceService;
import org.recordy.server.record.repository.RecordRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PreferenceServiceImpl implements PreferenceService {

    private final RecordRepository recordRepository;

    @Override
    public Preference getPreference(long userId) {
        return Preference.of(recordRepository.countAllByUserIdGroupByKeyword(userId));
    }
}
