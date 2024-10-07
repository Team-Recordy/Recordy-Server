package org.recordy.server.common.util;

import java.util.Objects;
import java.util.function.Function;

public class DomainUtils {

    public static <T> T updateIfNotNull(T existing, T update) {
        if (Objects.nonNull(update)) {
            return update;
        }

        return existing;
    }

    public static String updateIfNotEmpty(String existing, String update) {
        if (Objects.nonNull(update) && !update.isEmpty()) {
            return update;
        }

        return existing;
    }


    public static <T, R> R mapIfNotNull(T entity, Function<T, R> mapper) {
        if (Objects.nonNull(entity)) {
            return mapper.apply(entity);
        }

        return null;
    }
}
