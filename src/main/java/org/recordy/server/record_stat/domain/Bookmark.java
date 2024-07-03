package org.recordy.server.record_stat.domain;

import org.recordy.server.record.domain.Record;

public record Bookmark(
        long userId,
        Record record
) {
}
