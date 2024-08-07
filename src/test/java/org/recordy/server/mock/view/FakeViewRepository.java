package org.recordy.server.mock.view;

import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.view.domain.View;
import org.recordy.server.view.repository.ViewRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FakeViewRepository implements ViewRepository {

    public long autoIncrementId = 1L;
    public final Map<Long, View> views = new HashMap<>();

    @Override
    public View save(View view) {
        View realView = View.builder()
                .id(autoIncrementId)
                .user(view.getUser())
                .record(view.getRecord())
                .build();

        views.put(autoIncrementId++, realView);
        return view;
    }

    @Override
    public void deleteByUserId(long userId) {
        views.values()
                .removeIf(view -> view.getUser().getId() == userId);
    }

    @Override
    public Map<Keyword, Long> countAllByUserIdGroupByKeyword(long userId) {
        Map<Keyword, Long> totalKeywords = new HashMap<>();

        views.values().stream()
                .filter(view -> view.getUser().getId() == userId)
                .map(View::getRecord)
                .forEach(
                        record -> record.getKeywords()
                                .forEach(keyword -> {
                                    if (totalKeywords.containsKey(keyword))
                                        totalKeywords.put(keyword, totalKeywords.get(keyword) + 1);
                                    else
                                        totalKeywords.put(keyword, 1L);
                                })
                );

        return totalKeywords;
    }
}
