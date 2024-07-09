package org.recordy.server.record_stat.repository;

import org.recordy.server.record_stat.domain.Bookmark;

public interface BookmarkRepository {

    // command
    Bookmark save(Bookmark bookmark);
}
