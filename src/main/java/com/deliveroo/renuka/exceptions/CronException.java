package com.deliveroo.renuka.exceptions;

import com.deliveroo.renuka.parsers.FieldType;

public class CronException extends Exception{
    public CronException(String message) {
        super(message);
    }

    public CronException(FieldType fieldType, ErrorCode errorCode) {
        super(String.format("%s : %s", fieldType, errorCode.message));
    }

    public enum ErrorCode {
        NULL_INPUT("Cron expression is NULL"),
        EMPTY_INPUT("Cron expression is Empty"),
        INSUFFICIENT_PARAMS("Cron expression has missing fields"),
        LIST_RANGE_OUT_OF_BOUND("Invalid list range"),
        INVALID_NUMBER("Invalid number"),
        INVALID_RANGE("Invalid range"),
        TOO_MANY_ARGS("Multiple Ranges Detected"),
        INVALID_STEP("Invalid step");

        String message;

        ErrorCode(String message) {
            this.message = message;
        }
    }
}
