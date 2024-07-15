package org.recordy.server.record.controller.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import org.recordy.server.record.domain.Record;
import org.springframework.data.domain.Slice;

public record RecordInfoWithBookmark (
        RecordInfo recordInfo,
        Boolean isBookmark
){
    public static Slice<RecordInfoWithBookmark> of (Slice<Record> records, List<Boolean> bookmarks, Long currentUserId) {
        return records.map(record -> {
            int index = records.getContent().indexOf(record);
            Boolean isBookmarked = bookmarks.get(index);
            RecordInfo recordInfo = RecordInfo.from(record, currentUserId);
            return new RecordInfoWithBookmark(recordInfo, isBookmarked);
        });
    }

    public static List<RecordInfoWithBookmark> of (List<Record> records, List<Boolean> bookmarks, Long currentUserId) {
        return records.stream()
                .map(record -> {
                    int index = records.indexOf(record);
                    Boolean isBookmarked = bookmarks.get(index);
                    RecordInfo recordInfo = RecordInfo.from(record, currentUserId);
                    return new RecordInfoWithBookmark(recordInfo, isBookmarked);
                })
                .collect(Collectors.toCollection(java.util.ArrayList::new));
    }
}
