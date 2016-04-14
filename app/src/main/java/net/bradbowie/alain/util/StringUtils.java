package net.bradbowie.alain.util;

/**
 * Created by bradbowie on 4/11/16.
 */
public class StringUtils {
    public static boolean isValid(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static String guarantee(String s, String defaultIfInvalid) {
        return isValid(s) ? s : defaultIfInvalid;
    }

    public static String truncate(String s, int maxLength, String ellipse) {
        String g = guarantee(s, "");
        if (g.length() <= maxLength) return g;
        return ellipse != null && ellipse.length() < maxLength ? g.substring(0, maxLength - ellipse.length()) + ellipse : g.substring(0, maxLength);
    }
}
