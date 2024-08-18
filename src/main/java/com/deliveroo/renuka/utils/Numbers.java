package com.deliveroo.renuka.utils;

import com.deliveroo.renuka.parsers.FieldType;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Numbers {
    public static boolean isNumber(String str) {
        String regex = "^\\d+$";
        return str.matches(regex);
    }

    public static Map.Entry<Integer, Integer> getRange(FieldType fieldType) {
        return new AbstractMap.SimpleEntry<>(fieldType.start, fieldType.end);
    }

    public static String getListAsString(int number) {
        StringBuilder builder = new StringBuilder();
        for (int i = number; i <= number; i++) {
            builder.append(i);
        }
        return builder.toString().trim();
    }

    public static String getListAsString(int start, int end) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i <= end; i++) {
            builder.append(i);
            builder.append(" ");
        }
        return builder.toString().trim();
    }

    public static List<Integer> getListAsString(int start, int end, int step) {
        List<Integer> numbers = new ArrayList<>();
        for(int i = start; i <= end; i += step) {
            numbers.add(i);
        }
        return numbers;
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
