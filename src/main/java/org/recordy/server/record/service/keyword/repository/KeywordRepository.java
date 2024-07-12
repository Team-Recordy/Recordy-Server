package org.recordy.server.record.service.keyword.repository;

import org.recordy.server.record.service.keyword.domain.Keyword;

import java.util.List;

public interface KeywordRepository {

    // query
    List<Keyword> findAll();
}
