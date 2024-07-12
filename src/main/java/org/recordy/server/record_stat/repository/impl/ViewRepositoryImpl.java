package org.recordy.server.record_stat.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.keyword.domain.KeywordEntity;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.RecordEntity;
import org.recordy.server.record_stat.domain.View;
import org.recordy.server.record_stat.domain.ViewEntity;
import org.recordy.server.record_stat.repository.ViewRepository;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class ViewRepositoryImpl implements ViewRepository {

    private final ViewJpaRepository viewJpaRepository;
    private final ViewQueryDslRepository viewQueryDslRepository;

    @Override
    public View save(View view) {
        return viewJpaRepository.save(ViewEntity.from(view))
                .toDomain();
    }
}
