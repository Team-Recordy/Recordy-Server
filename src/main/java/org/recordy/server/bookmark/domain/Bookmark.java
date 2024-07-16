package org.recordy.server.bookmark.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.recordy.server.record.domain.Record;
import org.recordy.server.user.domain.User;

@AllArgsConstructor
@Builder
@Getter
public class Bookmark {

    private Long id;
    private User user;
    private Record record;
    private LocalDateTime createdAt;
}