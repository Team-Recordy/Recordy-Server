package org.recordy.server.mock.view;

import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.keyword.domain.KeywordEntity;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.RecordEntity;
import org.recordy.server.record.domain.UploadEntity;
import org.recordy.server.record_stat.domain.View;
import org.recordy.server.record_stat.domain.ViewEntity;
import org.recordy.server.record_stat.repository.ViewRepository;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

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
}
