package com.deliveroo.renuka.matchers;

import com.deliveroo.renuka.exceptions.CronException;
import com.deliveroo.renuka.parsers.FieldType;

import java.util.Map;

import static com.deliveroo.renuka.exceptions.CronException.ErrorCode.INVALID_RANGE;

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
                FieldType.MINUTE, new Rules("^(0?[0-9]|[1-5][0-9])-([0-5][0-9])$", FieldType.MINUTE, INVALID_RANGE),
                FieldType.HOUR, new Rules("^(0?[0-9]|1[0-9]|2[0-3])-(0?[0-9]|1[0-9]|2[0-3])$", FieldType.HOUR, INVALID_RANGE),
                FieldType.DAY_OF_MONTH, new Rules("^(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|[12][0-9]|3[0-1])$", FieldType.DAY_OF_MONTH, INVALID_RANGE),
                FieldType.MONTH, new Rules("^(0?[1-9]|1[0-2])-(0?[1-9]|1[0-2])$", FieldType.MONTH, INVALID_RANGE),
                FieldType.DAY_OF_WEEK, new Rules("^([1-7])-([1-7])$", FieldType.DAY_OF_WEEK, INVALID_RANGE)
        );
    }
}
