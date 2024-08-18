package com.deliveroo.renuka.parsers;

public class DayOfWeekParser extends TokenParser {

    public DayOfWeekParser(String token) {
        super(token);
    }

    @Override
    protected FieldType getFieldType() {
        return FieldType.DAY_OF_WEEK;
    }
}
