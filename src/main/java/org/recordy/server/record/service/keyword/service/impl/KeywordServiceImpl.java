package org.recordy.server.record.service.keyword.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.record.service.keyword.domain.Keyword;
import org.recordy.server.record.service.keyword.repository.KeywordRepository;
import org.recordy.server.record.service.keyword.service.KeywordService;
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
