package org.recordy.server.view.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ViewQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;
}
