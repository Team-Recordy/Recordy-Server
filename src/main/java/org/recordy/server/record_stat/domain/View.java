package org.recordy.server.record_stat.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.recordy.server.user.domain.User;
import org.recordy.server.record.domain.Record;

@AllArgsConstructor
@Builder
@Getter
public class View {

    private Long id;
    private User user;
    private Record record;
    private LocalDateTime createdAt;
}
