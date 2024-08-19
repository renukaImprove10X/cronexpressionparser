package com.deliveroo.renuka.parsers;

import com.deliveroo.renuka.exceptions.CronException;
import com.deliveroo.renuka.matchers.*;

public abstract class TokenParser {
    private final String token;

    public TokenParser(String token) {
        this.token = token;
    }

    protected String getToken() {
        return token;
    }

    protected final Regex findMatchingRegex() {
        Regex regex;
        if (getToken().equals("*")) {
            regex = new StarRegex(getToken(), getFieldType());
        } else if (getToken().contains("-")) {
            regex = new RangeRegex(getToken(), getFieldType());
        } else if (getToken().contains("*/")) {
            regex = new StepRegex(getToken(), getFieldType());
        } else if (getToken().contains(",")) {
            regex = new ListRegex(getToken(), getFieldType());
        } else {
            regex = new NumberRegex(getToken(), getFieldType());
        }
        return regex;
    }

    public String parse() throws CronException {
        Regex expression = findMatchingRegex();
        expression.validate();
        return expression.parse();
    }

    public String parseAndHandle(OnCronExceptionListener listener) {
        try {
            return parse();
        } catch (CronException cronException) {
            listener.onException(cronException);
        }
        return "";
    }

    protected abstract FieldType getFieldType();

}