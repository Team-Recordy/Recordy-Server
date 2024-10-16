package org.recordy.server.search.repository;

import org.recordy.server.search.domain.Search;

import java.util.List;

public interface SearchRepository {

    // command
    void save(Search search);

    // query
    List<Search> search(String query);
}
