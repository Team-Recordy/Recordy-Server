package org.recordy.server.common.dto.response;

import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Slice;

public record CursorBasePaginatedResponse<T>(
        Long nextCursor,
        boolean hasNext,
        List<T> content
) {

    public static <T> CursorBasePaginatedResponse<T> of(Slice<T> slice, Function<T, Long> idExtractor) {
        Long nextCursor = null;
        List<T> content = slice.getContent();

        if (!content.isEmpty()) {
            nextCursor = idExtractor.apply(content.get(content.size() - 1));
        }

        return new CursorBasePaginatedResponse<>(
                nextCursor,
                slice.hasNext(),
                content
        );
    }
}
