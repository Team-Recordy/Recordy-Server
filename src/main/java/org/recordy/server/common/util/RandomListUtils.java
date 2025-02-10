package org.recordy.server.common.util;

import java.util.Collections;
import java.util.List;

public class RandomListUtils {

    public static List<Long> getRandomSubList(List<Long> ids, int size) {
        Collections.shuffle(ids);
        return ids.subList(0, Math.min(size, ids.size()));
    }
}
