package com.spark.etl.core.util;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
}
