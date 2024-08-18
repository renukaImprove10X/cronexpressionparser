package com.deliveroo.renuka.matchers;

import com.deliveroo.renuka.exceptions.CronException;
import com.deliveroo.renuka.parsers.FieldType;

import java.util.Map;

import static com.deliveroo.renuka.exceptions.CronException.ErrorCode.INVALID_RANGE;

public class ListRegex extends Regex {
    public ListRegex(String token, FieldType fieldType) {
        super(token, fieldType);
    }

    @Override
    public String parse() throws CronException {
        String[] numbers = getFieldToken().split(",");
        for (String number : numbers) {
            int numeric = Integer.parseInt(number);
            isNumberInValid(numeric);
        }
        return getFieldToken().replace(",", " ");
    }

    private void isNumberInValid(int numeric) throws CronException {
        if (numeric < getFieldType().start || numeric > getFieldType().end) {
            throw new CronException(getFieldType(), CronException.ErrorCode.LIST_RANGE_OUT_OF_BOUND);
        }
    }

    @Override
    public Map<FieldType, Rules> getFieldRules() {
        return Map.of(
                FieldType.MINUTE, new Rules("^(0?[0-9]|[1-5][0-9])-([0-5][0-9])$", FieldType.MINUTE, INVALID_RANGE),
                FieldType.HOUR, new Rules("^(0?[0-9]|1[0-9]|2[0-3])-(0?[0-9]|1[0-9]|2[0-3])$", FieldType.HOUR, INVALID_RANGE),
                FieldType.DAY_OF_MONTH, new Rules("^(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|[12][0-9]|3[0-1])$", FieldType.DAY_OF_MONTH, INVALID_RANGE),
                FieldType.MONTH, new Rules("^(0?[1-9]|1[0-2])-(0?[1-9]|1[0-2])$", FieldType.MONTH, INVALID_RANGE),
                FieldType.DAY_OF_WEEK, new Rules("^([1-7])-([1-7])$", FieldType.DAY_OF_WEEK, INVALID_RANGE)
        );
    }
}
