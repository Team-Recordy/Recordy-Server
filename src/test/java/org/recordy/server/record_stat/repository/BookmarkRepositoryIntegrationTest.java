package org.recordy.server.record_stat.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record_stat.domain.Bookmark;
import org.recordy.server.util.DomainFixture;
import org.recordy.server.util.db.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

@SqlGroup({
        @Sql(value = "/sql/bookmark-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/bookmark-repository-test-clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@Transactional
@SpringBootTest
public class BookmarkRepositoryIntegrationTest extends IntegrationTest {
    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Test
    void delete를_통해_북마크_테이터를_생성할_수_있다() {
        //given
        Bookmark bookmark = DomainFixture.createBookmark();

        //when
        Bookmark result = bookmarkRepository.save(bookmark);

        //then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getUser().getId()).isEqualTo(DomainFixture.USER_ID),
                () -> assertThat(result.getRecord().getId()).isEqualTo(DomainFixture.RECORD_ID)
        );
    }

    @Test
    void findAllByBookmarksOrderByIdDesc를_통해_커서보다_오래된_레코드_데이터를_최신순으로_조회할_수_있다() {
        // given
        long userId = 1L;
        long cursor = 3L;
        int size = 10;

        // when
        Slice<Bookmark> result = bookmarkRepository.findAllByBookmarksOrderByIdDesc(userId, cursor, PageRequest.ofSize(size));

        // then
        assertAll(
                () -> assertThat(result.getContent()).hasSize(1),
                () -> assertThat(result.getContent().get(0).getId()).isEqualTo(1L),
                () -> assertThat(result.hasNext()).isFalse()
        );
    }

    @Test
    void findAllByBookmarksOrderByIdDesc를_통해_커서가_제일_오래된_값이라면_아무것도_반환되지_않는다() {
        // given
        long userId = 1L;
        long cursor = 1L;
        int size = 10;

        // when
        var result = bookmarkRepository.findAllByBookmarksOrderByIdDesc(userId, cursor, PageRequest.ofSize(size));

        // then
        assertAll(
                () -> assertThat(result.getContent()).isEmpty(),
                () -> assertThat(result.hasNext()).isFalse()
        );
    }

}
