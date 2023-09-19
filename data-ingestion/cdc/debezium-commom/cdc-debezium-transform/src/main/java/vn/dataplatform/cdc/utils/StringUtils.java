package vn.dataplatform.cdc.utils;

import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author tuan.nguyen3
 */
public class StringUtils {
    public StringUtils() {
    }
    public static List<String> getFields(String fields) {
        if (fields != null) {
            return Arrays.asList(fields.split(","));
        }
        return Collections.emptyList();
    }
    private static String checkParsing(String parsedString, String propertyName, Logger logger) {
        if (parsedString.startsWith("\"") || parsedString.startsWith("'")) {
            logger.warn("{} starts with a quotation mark. Please make sure it's intended", propertyName);
        }
        return parsedString;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
    public static String trimNotNull(String str){
        if (str == null) {
            return null;
        }
        return str.trim();
    }
    public static void main(String[] args){
        List<String> a = getFields("phone");
        System.out.println(a);
    }
}
