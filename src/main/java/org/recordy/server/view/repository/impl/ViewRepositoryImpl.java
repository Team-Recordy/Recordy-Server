package org.recordy.server.view.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.view.domain.View;
import org.recordy.server.view.domain.ViewEntity;
import org.recordy.server.view.repository.ViewRepository;
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

    @Override
    public void deleteByUserId(long userId) {
        viewJpaRepository.deleteAllByUserId(userId);
    }

    @Override
    public Map<Keyword, Long> countAllByUserIdGroupByKeyword(long userId) {
        return viewQueryDslRepository.countAllByUserIdGroupByKeyword(userId)
                .entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toDomain(),
                        Map.Entry::getValue
                ));
    }
}
