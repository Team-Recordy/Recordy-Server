package org.recordy.server.common.dto.response;

import org.springframework.data.domain.Slice;

import java.util.List;

public record OffsetBasePaginatedResponse<T>(
        int pageNumber,
        boolean hasNext,
        List<T> content
) {

    public static <T> OffsetBasePaginatedResponse<T> of(Slice<T> slice) {
        return new OffsetBasePaginatedResponse<>(
                slice.getNumber(),
                slice.hasNext(),
                slice.getContent()
        );
    }
}
