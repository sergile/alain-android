package net.bradbowie.alain.util;

import android.util.Log;

/**
 * Created by bradbowie on 4/11/16.
 */
public class LOG {

    protected static final String TAG_PREFIX = "alain.";
    protected static final String TAG_DEFAULT = TAG_PREFIX + "DEFAULT";
    protected static final int MAX_TAG_LENGTH = 23;

    public static String tag(Class<?> clazz) {
        return tag(clazz != null ? clazz.getSimpleName() : null);
    }

    public static String tag(String name) {
        return StringUtils.isValid(name) ? StringUtils.truncate(TAG_PREFIX + name, MAX_TAG_LENGTH, "..") : TAG_DEFAULT;
    }

    public static void v(String tag, String message) {
        Log.v(tag, message);
    }

    public static void d(String tag, String message) {
        Log.d(tag, message);
    }

    public static void i(String tag, String message) {
        Log.i(tag, message);
    }

    public static void w(String tag, String message) {
        Log.w(tag, message);
    }

    public static void e(String tag, String message) {
        LOG.e(tag, message);
    }

    public static void e(String tag, String message, Throwable t) {
        Log.e(tag, message, t);
    }
}
