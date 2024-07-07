package org.recordy.server.keyword.repository;

import org.recordy.server.keyword.domain.Keyword;

import java.util.List;

public interface KeywordRepository {

    // query
    List<Keyword> findAll();
}
