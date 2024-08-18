package com.deliveroo.renuka.matchers;

import com.deliveroo.renuka.exceptions.CronException;
import com.deliveroo.renuka.parsers.FieldType;
import com.deliveroo.renuka.utils.Numbers;

import java.util.Map;

import static com.deliveroo.renuka.exceptions.CronException.ErrorCode.INVALID_RANGE;

public class StepRegex extends Regex {
    public StepRegex(String token, FieldType fieldType) {
        super(token, fieldType);
    }

    @Override
    public String parse() throws CronException {
        String[] parts = getFieldToken().split("\\*/");
        handleExceptions(parts);
        int step = Integer.parseInt(parts[1]);
        checkIfInvalidStep(step);
        return Numbers.getListAsString(getFieldType().start, getFieldType().end, step);
    }

    private void checkIfInvalidStep(int step) throws CronException {
        if (step <= getFieldType().start || step > getFieldType().end) {
            throw new CronException(getFieldType(), CronException.ErrorCode.INVALID_STEP);
        }
    }

    private void handleExceptions(String[] parts) throws CronException {
        if (parts.length > 2)
            throw new CronException(getFieldType(), CronException.ErrorCode.TOO_MANY_ARGS);
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
