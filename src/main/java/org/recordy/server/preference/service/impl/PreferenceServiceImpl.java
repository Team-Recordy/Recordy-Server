package org.recordy.server.preference.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.bookmark.repository.BookmarkRepository;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.preference.domain.usecase.Preference;
import org.recordy.server.preference.service.PreferenceService;
import org.recordy.server.record.repository.UploadRepository;
import org.recordy.server.view.repository.ViewRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PreferenceServiceImpl implements PreferenceService {

    private final UploadRepository uploadRepository;
    private final ViewRepository viewRepository;
    private final BookmarkRepository bookmarkRepository;

    @Override
    public Preference getPreference(long userId) {
        Map<Keyword, Long> totalKeywords = new HashMap<>();

        addMap(totalKeywords, uploadRepository.countAllByUserIdGroupByKeyword(userId));
        addMap(totalKeywords, viewRepository.countAllByUserIdGroupByKeyword(userId));
        addMap(totalKeywords, bookmarkRepository.countAllByUserIdGroupByKeyword(userId));

        return Preference.of(totalKeywords);
    }

    private static void addMap(Map<Keyword, Long> totalKeywords, Map<Keyword, Long> keywords) {
        keywords.forEach((keyword, count) -> totalKeywords.merge(keyword, count, Long::sum));
    }
}
