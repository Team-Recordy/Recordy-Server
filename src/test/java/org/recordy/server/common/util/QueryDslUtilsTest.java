package org.recordy.server.common.util;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class QueryDslUtilsTest {

    @Test
    void ltCursorId를_통해_cursor가_null일_경우_null을_반환한다() {
        // given
        Long cursor = null;

        // when
        BooleanExpression result = QueryDslUtils.ltCursorId(cursor, null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void ltCursorId를_통해_id가_cursor보다_작은지_아닌지를_검증하는_쿼리를_반환한다() {
        // given
        Long cursor = 1L;
        NumberPath<Long> id = Expressions.numberPath(Long.class, "id");

        // when
        BooleanExpression result = QueryDslUtils.ltCursorId(cursor, id);

        // then
        assertThat(result.toString()).isEqualTo("id < " + cursor);
    }

    @Test
    void 데이터가_페이지보다_큰_경우_hasNext는_true를_반환한다() {
        // given
        int pageSize = 2;

        ArrayList<Integer> content = new ArrayList<>();
        content.add(1);
        content.add(2);
        content.add(3);

        // when
        boolean result = QueryDslUtils.hasNext(PageRequest.ofSize(pageSize), content);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 데이터가_페이지보다_큰_경우에도_hasNext는_페이지_사이즈만큼_반환한다() {
        // given
        int pageSize = 2;

        ArrayList<Integer> content = new ArrayList<>();
        content.add(1);
        content.add(2);
        content.add(3);

        // when
        QueryDslUtils.hasNext(PageRequest.ofSize(pageSize), content);

        // then
        assertThat(content.size()).isEqualTo(pageSize);
    }

    @Test
    void 데이터가_페이지보다_작거나_같을_경우_hasNext는_false를_반환한다() {
        // given
        int pageSize = 2;

        ArrayList<Integer> content = new ArrayList<>();
        content.add(1);
        content.add(2);

        // when
        boolean result = QueryDslUtils.hasNext(PageRequest.ofSize(pageSize), content);

        // then
        assertThat(result).isFalse();
    }
}