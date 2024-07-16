package org.recordy.server.view.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.view.domain.View;
import org.recordy.server.view.domain.ViewEntity;
import org.recordy.server.view.repository.ViewRepository;
import org.springframework.stereotype.Repository;

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
