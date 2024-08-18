package com.deliveroo.renuka.parsers;

import com.deliveroo.renuka.RangeRegex;
import com.deliveroo.renuka.Regex;
import com.deliveroo.renuka.StarRegex;
import com.deliveroo.renuka.exceptions.CronException;

public abstract class TokenParser {
    private final String token;

    public TokenParser(String token) {
        this.token = token;
    }

    protected String getToken() {
        return token;
    }

    protected final Regex findMatchingRegex() {
        Regex regex = null;
        if (getToken().equals("*")) {
            regex = new StarRegex(getToken(), getFieldType());
        }
        else if (getToken().contains("-")) {
            regex = new RangeRegex(getToken(), getFieldType());
        }
//        else if (getToken().contains("/")) {
//            regex = new StepRegex(getToken(), getFieldType());
//        } else if (getToken().contains(",")) {
//            regex = new CommaRegex(getToken(), getFieldType());
//        } else {
//            regex = new NumberRegex(getToken(), getFieldType());
//        }
        return regex;
    }

    public String parse() throws CronException {
        Regex expression = findMatchingRegex();
        expression.validate();
        return expression.parse();
    }

    protected abstract FieldType getFieldType();

}

