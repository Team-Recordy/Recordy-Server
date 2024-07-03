package org.recordy.server.mock.keyword;

import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.keyword.repository.KeywordRepository;

import java.util.List;

public class FakeKeywordRepository implements KeywordRepository {

    @Override
    public List<Keyword> findAll() {
        return List.of(Keyword.values());
    }
}
