package org.recordy.server.record_stat.repository;

import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record_stat.domain.View;
import org.recordy.server.user.domain.User;

import java.util.Map;

public interface ViewRepository {

    // command
    View save(View view);

    // query
}
