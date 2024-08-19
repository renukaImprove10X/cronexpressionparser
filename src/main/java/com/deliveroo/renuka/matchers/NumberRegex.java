package com.deliveroo.renuka.matchers;

import com.deliveroo.renuka.exceptions.CronException;
import com.deliveroo.renuka.parsers.FieldType;

import java.util.Map;

import static com.deliveroo.renuka.exceptions.CronException.ErrorCode.INVALID_NUMBER;

public class NumberRegex extends Regex {
    public NumberRegex(String token, FieldType fieldType) {
        super(token, fieldType);
    }

    @Override
    public String parse() throws CronException {
        int number = Integer.parseInt(getFieldToken());
        handleExceptions(number);
        return number+"".trim();
    }

    private void handleExceptions(int number) throws CronException {
        if (number < getFieldType().start || number > getFieldType().end) {
            throw new CronException(getFieldType(), CronException.ErrorCode.INVALID_NUMBER);
        }
    }

    @Override
    public Map<FieldType, Rules> getFieldRules() {
        return Map.of(
                FieldType.MINUTE, new Rules("^(?:[0-5]?\\d)$", FieldType.MINUTE, INVALID_NUMBER),
                FieldType.HOUR, new Rules("^(?:[01]?\\d|2[0-3])$", FieldType.HOUR, INVALID_NUMBER),
                FieldType.DAY_OF_MONTH, new Rules("^(?:0?[1-9]|[12]\\d|3[01])$", FieldType.DAY_OF_MONTH, INVALID_NUMBER),
                FieldType.MONTH, new Rules("^(?:0?[1-9]|1[0-2])$", FieldType.MONTH, INVALID_NUMBER),
                FieldType.DAY_OF_WEEK, new Rules("^[0-6]$", FieldType.DAY_OF_WEEK, INVALID_NUMBER)
        );
    }
}
