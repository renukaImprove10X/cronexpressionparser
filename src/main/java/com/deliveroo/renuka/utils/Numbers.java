package com.deliveroo.renuka.utils;

import com.deliveroo.renuka.parsers.FieldType;

import java.util.AbstractMap;
import java.util.Map;

public class Numbers {
    public static boolean isNumber(String str) {
        String regex = "^\\d+$";
        return str.matches(regex);
    }

    public static Map.Entry<Integer, Integer> getRange(FieldType fieldType) {
        return new AbstractMap.SimpleEntry<>(fieldType.start, fieldType.end);
    }

    public static String getListAsString(int start, int end) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i <= end; i++) {
            builder.append(i);
            builder.append(" ");
        }
        return builder.toString().trim();
    }

    public static String getListAsString(int start, int end, int step) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i <= end; i+=step) {
            builder.append(i);
            builder.append(" ");
        }
        return builder.toString().trim();
    }
}
