package org.recordy.server.keyword.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.keyword.domain.KeywordEntity;
import org.recordy.server.keyword.repository.KeywordRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class KeywordRepositoryImpl implements KeywordRepository {

    private final KeywordJpaRepository keywordJpaRepository;

    @Override
    public List<Keyword> findAll() {
        return keywordJpaRepository.findAll().stream()
                .map(KeywordEntity::toDomain)
                .toList();
    }
}
