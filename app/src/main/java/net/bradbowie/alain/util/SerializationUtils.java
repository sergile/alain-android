package net.bradbowie.alain.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bradbowie on 4/11/16.
 */
public class SerializationUtils {
    private static final String TAG = LOG.tag(SerializationUtils.class);
    private static final ObjectMapper JACKSON;

    static {
        JACKSON = new ObjectMapper();
        JACKSON.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JACKSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Serializes an object into a JSON String. Returns null on error
     */
    public static String toJsonSafe(Object obj) {
        if (obj == null) return null;

        try {
            return JACKSON.writeValueAsString(obj);
        } catch (Exception e) {
            LOG.e(TAG, "Swallowing error while serializing " + obj.getClass().getSimpleName() + " to JSON", e);
            return null;
        }
    }

    /**
     * Deserializes a JSON object into the {@param expectedType}. Returns null on error
     */
    public static <T> T fromJsonSafe(String json, Class<T> expectedType) {
        if (json == null) return null;

        try {
            return JACKSON.readValue(json, expectedType);
        } catch (Exception e) {
            LOG.e(TAG, "Swallowing error while constructing " + expectedType.getSimpleName() + " from JSON", e);
            LOG.v(TAG, "Failed JSON: " + json);
            return null;
        }
    }

    /**
     * Deserializes a JSON object into a list of {@param expectedType}. Returns null on error
     */
    public static <T> List<T> fromJsonToListSafe(String json, Class<T> expectedType) {
        if (json == null) return null;

        try {
            CollectionType typeFactory = JACKSON.getTypeFactory().constructCollectionType(List.class, expectedType);
            return JACKSON.readValue(json, typeFactory);
        } catch (Exception e) {
            LOG.e(TAG, "Swallowing error while constructing a List of " + expectedType.getSimpleName() + " from JSON", e);
            LOG.v(TAG, "Failed JSON: " + json);
            return null;
        }
    }

    /**
     * Deserializes a JSON object into a list of {@param expectedType}. Returns null on error
     */
    public static <K, V> Map<K, V> fromJsonToMapSafe(String json, Class<K> keyType, Class<V> valueType) {
        if (json == null) return null;

        try {
            MapType typeFactory = JACKSON.getTypeFactory().constructMapType(LinkedHashMap.class, keyType, valueType);
            return JACKSON.readValue(json, typeFactory);
        } catch (Exception e) {
            LOG.e(TAG, "Swallowing error while constructing a Map of " + valueType.getSimpleName() + " from JSON", e);
            LOG.v(TAG, "Failed JSON: " + json);
            return null;
        }
    }
}
