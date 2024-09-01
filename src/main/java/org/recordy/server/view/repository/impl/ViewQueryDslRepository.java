package org.recordy.server.view.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.recordy.server.view.domain.ViewEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.recordy.server.view.domain.QViewEntity.viewEntity;

@RequiredArgsConstructor
@Repository
public class ViewQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<ViewEntity> findAllByUserId(long userId) {
        return jpaQueryFactory
                .selectFrom(viewEntity)
                .where(
                        viewEntity.user.id.eq(userId)
                )
                .fetch();
    }
}
