package org.recordy.server.report.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.recordy.server.report.domain.Report;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static org.recordy.server.report.domain.QReport.report;

@RequiredArgsConstructor
@Repository
public class ReportQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<Report> findById(long reportId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(report)
                        .where(
                                report.id.eq(reportId)
                        )
                        .fetchOne()
        );
    }

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

    public Optional<Report> findByReporterIdAndRecordId(long reporterId, long recordId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(report)
                        .where(
                                report.reporter.id.eq(reporterId),
                                report.record.id.eq(recordId)
                        )
                        .fetchOne()
        );
    }
}
