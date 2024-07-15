package org.recordy.server.subscribe.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.recordy.server.user.domain.User;

@AllArgsConstructor
@Builder
@Getter
public class Subscribe {

    private Long id;
    private User subscribingUser;
    private User subscribedUser;
}
