package org.recordy.server.mock.view;

import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.keyword.domain.KeywordEntity;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.RecordEntity;
import org.recordy.server.record.domain.UploadEntity;
import org.recordy.server.record_stat.domain.ViewEntity;
import org.recordy.server.record_stat.repository.ViewRepository;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class FakeViewRepository implements ViewRepository {

    public long autoIncrementId = 1L;
    public final Map<Long, ViewEntity> viewEntities = new HashMap<>();

    @Override
    public void save(Record record, User user) {
        ViewEntity viewEntity = ViewEntity.builder()
                .id(autoIncrementId)
                .record(RecordEntity.from(record))
                .user(UserEntity.from(user))
                .build();

        viewEntities.put(autoIncrementId++, viewEntity);
    }

    @Override
    public Map<Keyword, Long> countAllByUserIdGroupByKeyword(long userId) {
        Map<Keyword, Long> keywordCounts = new HashMap<>();

        viewEntities.values().stream()
                .filter(viewEntity -> viewEntity.getUser().getId().equals(userId))
                .map(ViewEntity::getRecord)
                .map(RecordEntity::getUploads)
                .flatMap(uploads -> uploads.stream().map(UploadEntity::getKeyword))
                .forEach(keyword -> {
                    if (!keywordCounts.containsKey(keyword)) {
                        keywordCounts.put(keyword.toDomain(), 0L);
                    }
                    keywordCounts.put(keyword.toDomain(), keywordCounts.get(keyword) + 1);
                });

        return keywordCounts;
    }
}
