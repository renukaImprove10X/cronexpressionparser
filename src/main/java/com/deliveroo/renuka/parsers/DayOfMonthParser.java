package com.deliveroo.renuka.parsers;

public class DayOfMonthParser extends TokenParser {

    public DayOfMonthParser(String token) {
        super(token);
    }

    @Override
    protected FieldType getFieldType() {
        return FieldType.DAY_OF_MONTH;
    }
}
