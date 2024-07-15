package org.recordy.server.common.dto.response;

import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Slice;

public record PaginatedResponse<T> (
        int pageNumber,
        boolean hasNext,
        List<T> content
){
    public static <T> PaginatedResponse<T> of(Slice<T> slice) {

        return new PaginatedResponse<>(
                slice.getNumber(),
                slice.hasNext(),
                slice.getContent()
        );
    }
}
