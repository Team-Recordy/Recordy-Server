package org.recordy.server.view.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.view.domain.View;
import org.recordy.server.view.domain.ViewEntity;
import org.recordy.server.view.repository.ViewRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public List<View> findAllByUserId(long userId) {
        return viewQueryDslRepository.findAllByUserId(userId).stream()
                .map(ViewEntity::toDomain)
                .toList();
    }
}
