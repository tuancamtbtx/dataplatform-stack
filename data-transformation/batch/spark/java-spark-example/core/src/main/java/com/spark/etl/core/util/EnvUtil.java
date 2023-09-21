package com.spark.etl.core.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EnvUtil {
    public static String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public static String getEnv(String key) {
        return getEnv(key, null);
    }

    public static String getEnv(String key, int defaultValue) {
        String value = System.getenv(key);
        if (value == null) {
            return String.valueOf(defaultValue);
        }
        return value;
    }

    public static String getEnv(String key, long defaultValue) {
        String value = System.getenv(key);
        if (value == null) {
            return String.valueOf(defaultValue);
        }
        return value;
    }

    public static String getEnv(String key, double defaultValue) {
        String value = System.getenv(key);
        if (value == null) {
            return String.valueOf(defaultValue);
        }
        return value;
    }

    public static String getEnv(String key, boolean defaultValue) {
        String value = System.getenv(key);
        if (value == null) {
            return String.valueOf(defaultValue);
        }
        return value;
    }
}
