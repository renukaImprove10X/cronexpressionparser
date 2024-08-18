package com.deliveroo.renuka.matchers;

import com.deliveroo.renuka.exceptions.CronException;
import com.deliveroo.renuka.parsers.FieldType;
import com.deliveroo.renuka.utils.Numbers;

import java.util.Map;

import static com.deliveroo.renuka.exceptions.CronException.ErrorCode.INVALID_RANGE;
import static com.deliveroo.renuka.utils.Numbers.isNumber;

public class RangeRegex extends Regex {
    public RangeRegex(String token, FieldType fieldType) {
        super(token, fieldType);
    }

    @Override
    public String parse() throws CronException {
        String[] interval = getFieldToken().split("-");
        handleExceptions(interval);
        int startInterval = Integer.parseInt(interval[0]);
        int endInterval = Integer.parseInt(interval[1]);
        checkForInValidRangeException(startInterval < getFieldType().start, endInterval > getFieldType().end);
        return Numbers.getListAsString(startInterval, endInterval);
    }

    private void checkForInValidRangeException(boolean startInterval, boolean endInterval) throws CronException {
        if (startInterval || endInterval) {
            throw new CronException(getFieldType(), INVALID_RANGE);
        }
    }

    private void handleExceptions(String[] interval) throws CronException {
        if (interval.length > 2)
            throw new CronException(getFieldType(), CronException.ErrorCode.TOO_MANY_ARGS);
        checkForInValidRangeException(!isNumber(interval[0]), !isNumber(interval[1]));
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
