package org.recordy.server.mock.view;

import org.recordy.server.view.domain.View;
import org.recordy.server.view.repository.ViewRepository;

import java.util.HashMap;
import java.util.Map;

public class FakeViewRepository implements ViewRepository {

    public long autoIncrementId = 1L;
    public final Map<Long, View> viewEntities = new HashMap<>();

    @Override
    public View save(View view) {
        View realView = View.builder()
                .id(autoIncrementId)
                .user(view.getUser())
                .record(view.getRecord())
                .build();

        viewEntities.put(autoIncrementId++, realView);
        return view;
    }

    @Override
    public void deleteByUserId(long userId) {
        viewEntities.values()
                .removeIf(view -> view.getUser().getId() == userId);
    }
}
