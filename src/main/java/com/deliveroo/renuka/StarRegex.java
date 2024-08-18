package com.deliveroo.renuka;

import com.deliveroo.renuka.parsers.FieldType;
import com.deliveroo.renuka.utils.Numbers;

import java.util.Map;

public class StarRegex extends Regex{


    public StarRegex(String fieldToken, FieldType fieldType) {
        super(fieldToken, fieldType);
    }

    @Override
    public String parse(){
        return Numbers.getListAsString(getFieldType().start, getFieldType().end);
    }

    @Override
    public Map<FieldType, Rules> getFieldRules() {
        return Map.of();
    }

}
