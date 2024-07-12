package org.recordy.server.record.service.keyword.service;

import org.recordy.server.record.service.keyword.domain.Keyword;

import java.util.List;

public interface KeywordService {

    // query
    List<Keyword> getAll();
}
