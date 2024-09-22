package org.recordy.server.common.dto.response;

import java.util.List;
import org.springframework.data.domain.Slice;

public record CursorBasePaginatedResponse<T>(
        Long nextCursor,
        boolean hasNext,
        List<T> content
) {

    public static <T extends CursorResponse> CursorBasePaginatedResponse<T> of(Slice<T> slice) {
        return new CursorBasePaginatedResponse<>(
                slice.getContent().isEmpty() ? null : slice.getContent().get(slice.getSize() - 1).getId(),
                slice.hasNext(),
                slice.getContent()
        );
    }
}
