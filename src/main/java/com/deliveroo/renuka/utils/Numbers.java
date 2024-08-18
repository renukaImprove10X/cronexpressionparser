package com.deliveroo.renuka.utils;

import com.deliveroo.renuka.FieldType;

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

    public static String getListString(FieldType fieldType) {
        StringBuilder builder = new StringBuilder();
        for (int i = fieldType.start; i <= fieldType.end; i++) {
            builder.append(i);
            builder.append(" ");
        }
        return builder.toString().trim();
    }
}
