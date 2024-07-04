package org.recordy.server.common.util;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class QueryDslUtils {

    public static BooleanExpression goeCursorId(Long cursor, NumberPath<Long> id) {
        return cursor == null ? null : id.goe(cursor);
    }

    public static <T> boolean hasNext(Pageable pageable, List<T> content) {
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            return true;
        }

        return false;
    }
}
