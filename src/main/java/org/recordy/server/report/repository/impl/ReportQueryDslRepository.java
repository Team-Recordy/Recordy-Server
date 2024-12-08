package org.recordy.server.report.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static org.recordy.server.report.domain.QReport.report;

@RequiredArgsConstructor
@Repository
public class ReportQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Long countAllByRecordIdAndCreatedAfter(long recordId, LocalDateTime from) {
        return jpaQueryFactory
                .select(report.id.count())
                .from(report)
                .where(
                        report.record.id.eq(recordId),
                        report.createdAt.after(from)
                )
                .fetchFirst();
    }
}
