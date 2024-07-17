package org.recordy.server.bookmark.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.recordy.server.bookmark.repository.BookmarkRepository;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.bookmark.domain.Bookmark;
import org.recordy.server.user.domain.User;
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
        @Sql(value = "/sql/clean-database.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(value = "/sql/bookmark-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/clean-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@Transactional
@SpringBootTest
public class BookmarkRepositoryIntegrationTest extends IntegrationTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private RecordRepository recordRepository;

    @Test
    void save를_통해_북마크_테이터를_생성할_수_있다() {
        //given
        Bookmark bookmark = DomainFixture.createBookmark();

        //when
        Bookmark result = bookmarkRepository.save(bookmark);

        //then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getUser().getId()).isEqualTo(DomainFixture.USER_ID),
                () -> assertThat(result.getRecord().getId()).isEqualTo(DomainFixture.RECORD_ID),
                () -> assertThat(result.getRecord().getBookmarkCount()).isEqualTo(1)
        );
    }

    @Test
    void save를_통해_저장된_북마크_데이터는_레코드_조회_시_데이터_개수로_카운트될_수_있다() {
        // given
        // userId 1 <-> recordId 1
        // userId 1 <-> recordId 2
        // userId 2 <-> recordId 1
        // userId 2 <-> recordId 1

        // when
        Slice<Record> result = recordRepository.findAllByIdAfterOrderByIdDesc(0, PageRequest.ofSize(4));

        // then
        assertAll(
                () -> assertThat(result.getContent().size()).isEqualTo(2),
                () -> assertThat(result.getContent().get(0).getBookmarkCount()).isEqualTo(2),
                () -> assertThat(result.getContent().get(1).getBookmarkCount()).isEqualTo(2)
        );
    }

    @Test
    void delete를_통해_북마크를_삭제할_수_있다() {
        // given, when
        bookmarkRepository.delete(1, 1);
        Slice<Bookmark> bookmarks = bookmarkRepository.findAllByBookmarksOrderByIdDesc(1L, 4L, PageRequest.ofSize(10));

        // then
        assertAll(
                () -> assertThat(bookmarks.getContent()).hasSize(1)
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
        Slice<Bookmark> result = bookmarkRepository.findAllByBookmarksOrderByIdDesc(userId, cursor, PageRequest.ofSize(size));

        // then
        assertAll(
                () -> assertThat(result.getContent()).isEmpty(),
                () -> assertThat(result.hasNext()).isFalse()
        );
    }

    @Test
    void existsByUserIdAndRecordId를_통해_주어진_userId와_recordId로_bookmark가_존재하면_true를_반환한다() {
        //given
        User user = DomainFixture.createUser(3) ;
        Record record = DomainFixture.createRecord();
        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .record(record)
                .build();
        bookmarkRepository.save(bookmark);

        //when
        Boolean result = bookmarkRepository.existsByUserIdAndRecordId(user.getId(), record.getId());

        //then
        assertAll(
                () -> assertThat(result).isTrue()
        );
    }

    @Test
    void existsByUserIdAndRecordId를_통해_주어진_userId와_recordId로_bookmark가_존재하지_않으면_false를_반환한다() {
        //given
        User user = DomainFixture.createUser(3) ;
        Record record = DomainFixture.createRecord();

        //when
        Boolean result = bookmarkRepository.existsByUserIdAndRecordId(user.getId(), record.getId());

        //then
        assertAll(
                () -> assertThat(result).isFalse()
        );
    }
}
