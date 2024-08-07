package org.recordy.server.view.repository;

import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.view.domain.View;

import java.util.Map;

public interface ViewRepository {

    // command
    View save(View view);
    void deleteByUserId(long userId);

    // query
    Map<Keyword, Long> countAllByUserIdGroupByKeyword(long userId);
}
