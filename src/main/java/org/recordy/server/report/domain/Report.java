package org.recordy.server.report.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.recordy.server.common.domain.JpaMetaInfoEntity;
import org.recordy.server.record.domain.RecordEntity;
import org.recordy.server.user.domain.UserEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "reports")
@Entity
public class Report extends JpaMetaInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private RecordEntity record;

    private ReportReason reason;
    private String content;
    private ApprovalStatus approvalStatus;

    private Report(
            UserEntity reporter,
            RecordEntity record,
            ReportReason reason,
            String content
    ) {
        this.reporter = reporter;
        this.record = record;
        this.reason = reason;
        this.content = content;
        this.approvalStatus = ApprovalStatus.PENDING;
    }

    public static Report create(ReportCreate create) {
        return new Report(
                UserEntity.of(create.reporterId()),
                RecordEntity.of(create.recordId()),
                create.reason(),
                create.content()
        );
    }

    public void resolve(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
        if (approvalStatus == ApprovalStatus.APPROVED) {
            record.block();
        }
        else if (approvalStatus == ApprovalStatus.DISMISSED){
            record.nonBlock();
        }
    }
}
