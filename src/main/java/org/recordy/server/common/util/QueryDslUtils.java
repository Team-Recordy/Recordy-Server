package org.recordy.server.common.util;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import org.recordy.server.record.domain.RecordEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class QueryDslUtils {

    public static BooleanExpression ltCursorId(Long cursor, NumberPath<Long> id) {
        return cursor == null ? null : id.lt(cursor);
    }

    public static <T> boolean hasNext(Pageable pageable, List<T> content) {
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            return true;
        }

        return false;
    }

    public static long cursorIdCheck(Long cursor){
        if (cursor > 0) {
            return cursor;
        }
        return Long.MAX_VALUE;
    }
}
