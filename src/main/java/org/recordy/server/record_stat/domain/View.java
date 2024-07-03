package org.recordy.server.record_stat.domain;

import org.recordy.server.record.domain.Record;

public record View(
        long userId,
        Record record
) {
}
