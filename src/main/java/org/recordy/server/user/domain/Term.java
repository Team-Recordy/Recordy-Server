package org.recordy.server.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class Term {
    String content;
    boolean check;
}
