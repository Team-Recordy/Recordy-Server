package org.recordy.server.mock.search;

import org.recordy.server.search.domain.Search;
import org.recordy.server.search.repository.SearchRepository;

import java.util.List;

public class FakeSearchRepository implements SearchRepository {

    @Override
    public void save(Search search) {

    }

    @Override
    public List<Search> search(String query) {
        return List.of();
    }
}
