package com.deliveroo.renuka.matchers;

import com.deliveroo.renuka.exceptions.CronException;
import com.deliveroo.renuka.parsers.FieldType;

import java.util.Map;

import static com.deliveroo.renuka.exceptions.CronException.ErrorCode.LIST_RANGE_OUT_OF_BOUND;

public class ListRegex extends Regex {
    public ListRegex(String token, FieldType fieldType) {
        super(token, fieldType);
    }

    @Override
    public String parse() throws CronException {
        String[] numbers = getFieldToken().split(",");
        return getFieldToken().replace(",", " ");
    }

    @Override
    public Map<FieldType, Rules> getFieldRules() {
        return Map.of(
                FieldType.MINUTE, new Rules("^(?:[0-5]?\\d)(?:,(?:[0-5]?\\d))*$", FieldType.MINUTE, LIST_RANGE_OUT_OF_BOUND),
                FieldType.HOUR, new Rules("^(?:[01]?\\d|2[0-3])(?:,(?:[01]?\\d|2[0-3]))*$", FieldType.HOUR, LIST_RANGE_OUT_OF_BOUND),
                FieldType.DAY_OF_MONTH, new Rules("^(?:0?[1-9]|[12]\\d|3[01])(?:,(?:0?[1-9]|[12]\\d|3[01]))*$", FieldType.DAY_OF_MONTH, LIST_RANGE_OUT_OF_BOUND),
                FieldType.MONTH, new Rules("^(?:0?[1-9]|1[0-2])(?:,(?:0?[1-9]|1[0-2]))*$", FieldType.MONTH, LIST_RANGE_OUT_OF_BOUND),
                FieldType.DAY_OF_WEEK, new Rules("^[0-6](?:,[0-6])*$", FieldType.DAY_OF_WEEK, LIST_RANGE_OUT_OF_BOUND)
        );
    }
}
