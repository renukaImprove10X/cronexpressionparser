package com.deliveroo.renuka.parsers;

public enum FieldType{
    MINUTE("minute", 0, 59),
    HOUR("hour", 0, 23),
    DAY_OF_MONTH("day of month", 1, 31),
    MONTH("month", 1, 12),
    DAY_OF_WEEK("day of week", 1, 7),
    COMMAND("command", 0, 0),
    CRON_EXPRESSION("input", 0, 0);

    public final String label;
    public final int start;
    public final int end;

    FieldType(String label, int start, int end) {
        this.label = label;
        this.start = start;
        this.end = end;
    }
}

// packaging
// use start and end in range
// utils
// put all instance variables as private
// new modifications
// bifurcate pattern and fields


// Divide Prser and tokenizer
//

