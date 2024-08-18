package com.deliveroo.renuka.matchers;

import com.deliveroo.renuka.exceptions.CronException;
import com.deliveroo.renuka.parsers.FieldType;

import java.util.Map;

import static com.deliveroo.renuka.exceptions.CronException.ErrorCode.INVALID_RANGE;

public abstract class Regex {
    private final FieldType fieldType;
    private final String fieldToken;

    public Regex(String fieldToken, FieldType fieldType) {
        this.fieldToken = fieldToken;
        this.fieldType = fieldType;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public String getFieldToken() {
        return fieldToken;
    }

    public void validate() throws CronException {
        Rules rules = getFieldRules().get(getFieldType());
        if (rules != null && !fieldToken.matches(rules.regex())) {
            throw new CronException(rules.fieldType() + " " + rules.errorCode());
        }
    }

    public abstract String parse() throws CronException;

    public abstract Map<FieldType, Rules> getFieldRules();
}
