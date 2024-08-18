package com.deliveroo.renuka.parsers;

public class MonthParser extends TokenParser {

    public MonthParser(String token) {
        super(token);
    }

    @Override
    protected FieldType getFieldType() {
        return FieldType.MONTH;
    }
}
