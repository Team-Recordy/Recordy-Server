package org.recordy.server.keyword.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.keyword.repository.KeywordRepository;
import org.recordy.server.keyword.service.KeywordService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class KeywordServiceImpl implements KeywordService {

    private final KeywordRepository keywordRepository;

    @Override
    public List<Keyword> getAll() {
        return keywordRepository.findAll();
    }
}
