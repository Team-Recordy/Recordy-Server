package org.recordy.server.record.controller.dto.response;

import java.util.List;
import org.recordy.server.record.domain.Record;
import org.springframework.data.domain.Slice;

public record RecordInfoWithBookmark (
        RecordInfo recordInfo,
        Boolean isBookmark
){
    public static Slice<RecordInfoWithBookmark> of (Slice<Record> records, List<Boolean> bookmarks) {
        return records.map(record -> {
            int index = records.getContent().indexOf(record);
            Boolean isBookmarked = bookmarks.get(index);
            RecordInfo recordInfo = RecordInfo.from(record);
            return new RecordInfoWithBookmark(recordInfo, isBookmarked);
        });
    }
}
