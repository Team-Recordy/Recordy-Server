package org.recordy.server.common.util;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import org.springframework.data.domain.Pageable;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static org.recordy.server.exhibition.domain.QExhibitionEntity.exhibitionEntity;

public class QueryDslUtils {

    public static final Predicate[] hasOngoingExhibitions = {
            exhibitionEntity.endDate.goe(LocalDate.now(Clock.systemDefaultZone())),
            exhibitionEntity.startDate.loe(LocalDate.now(Clock.systemDefaultZone()))
    };

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

    public static <T> boolean hasNext(int size, List<T> content) {
        if (content.size() > size) {
            content.remove(size);
            return true;
        }

        return false;
    }
}
