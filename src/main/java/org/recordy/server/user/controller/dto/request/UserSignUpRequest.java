package org.recordy.server.user.controller.dto.request;

import org.recordy.server.user.domain.Term;
import org.recordy.server.user.domain.TermEntity;

import java.util.List;

public record UserSignUpRequest(
        String nickname,
        List<Term> term
) {
}
