package org.recordy.server.record.domain;

import org.junit.jupiter.api.Test;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.keyword.domain.KeywordEntity;
import org.recordy.server.util.DomainFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UploadEntityTest {

    @Test
    void of를_통해_RecordEntity와_KeywordEntity를_토대로_UploadEntity를_생성할_수_있다() {
        // given
        RecordEntity recordEntity = DomainFixture.createRecordEntity();
        KeywordEntity keywordEntity = new KeywordEntity(Keyword.감각적인);

        // when
        UploadEntity uploadEntity = UploadEntity.of(recordEntity, keywordEntity);

        // then
        assertAll(
                () -> assertThat(uploadEntity.getRecord()).isEqualTo(recordEntity),
                () -> assertThat(uploadEntity.getKeyword()).isEqualTo(keywordEntity)
        );
    }
}