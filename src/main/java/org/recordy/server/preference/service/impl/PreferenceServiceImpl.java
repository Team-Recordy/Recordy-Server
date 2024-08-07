package org.recordy.server.preference.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.bookmark.repository.BookmarkRepository;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.preference.domain.Preference;
import org.recordy.server.preference.service.PreferenceService;
import org.recordy.server.record.repository.UploadRepository;
import org.recordy.server.view.repository.ViewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PreferenceServiceImpl implements PreferenceService {

    private final UploadRepository uploadRepository;
    private final ViewRepository viewRepository;
    private final BookmarkRepository bookmarkRepository;

    @Override
    public Preference getPreference(long userId) {
        Map<Keyword, Long> totalKeywordAndCounts = new HashMap<>();

        addMap(totalKeywordAndCounts, uploadRepository.countAllByUserIdGroupByKeyword(userId));
        addMap(totalKeywordAndCounts, viewRepository.countAllByUserIdGroupByKeyword(userId));
        addMap(totalKeywordAndCounts, bookmarkRepository.countAllByUserIdGroupByKeyword(userId));

        return Preference.from(totalKeywordAndCounts);
    }

    private static void addMap(Map<Keyword, Long> totalKeywords, Map<Keyword, Long> keywords) {
        keywords.forEach((keyword, count) -> totalKeywords.merge(keyword, count, Long::sum));
    }
}
