package net.bradbowie.alain.util;

import java.util.Collection;
import java.util.Map;

/**
 * Created by bradbowie on 4/11/16.
 */
public class CollectionUtils {
    public static boolean isValid(Collection<?> coll) {
        return coll != null && !coll.isEmpty();
    }

    public static boolean isValid(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    public static boolean isValid(Object[] arr) {
        return arr != null && arr.length > 0;
    }

    public static int size(Collection<?> coll) {
        return CollectionUtils.isValid(coll) ? coll.size() : 0;
    }
}
