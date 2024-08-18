package com.deliveroo.renuka.parsers;

public class MinutesParser extends TokenParser {

    public MinutesParser(String token) {
        super(token);
    }

    @Override
    protected FieldType getFieldType() {
        return FieldType.MINUTE;
    }
}
