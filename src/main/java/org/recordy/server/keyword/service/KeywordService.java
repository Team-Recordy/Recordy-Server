package org.recordy.server.keyword.service;

import org.recordy.server.keyword.domain.Keyword;

import java.util.List;

public interface KeywordService {

    // query
    List<Keyword> getAll();
}
