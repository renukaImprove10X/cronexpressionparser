package com.deliveroo.renuka.parsers;

public class HoursParser extends TokenParser {

    public HoursParser(String token) {
        super(token);
    }

    @Override
    protected FieldType getFieldType() {
        return FieldType.HOUR;
    }
}
