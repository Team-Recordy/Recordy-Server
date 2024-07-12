package org.recordy.server.record_stat.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.recordy.server.keyword.domain.KeywordEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.recordy.server.record.domain.QRecordEntity.recordEntity;
import static org.recordy.server.record.domain.QUploadEntity.uploadEntity;
import static org.recordy.server.record_stat.domain.QViewEntity.viewEntity;

@RequiredArgsConstructor
@Repository
public class ViewQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;
}
