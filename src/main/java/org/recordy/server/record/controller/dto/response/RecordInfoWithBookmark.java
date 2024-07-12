package org.recordy.server.record.controller.dto.response;

public record RecordInfoWithBookmark (
        RecordInfo recordInfo,
        Boolean isBookmark
){

}
