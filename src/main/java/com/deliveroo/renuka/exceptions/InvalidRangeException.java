package com.deliveroo.renuka.exceptions;

import com.deliveroo.renuka.FieldType;

public class InvalidRangeException extends CronException {

    public InvalidRangeException(FieldType fieldType, ErrorCode errorCode){
        super(String.format("%s : %s", fieldType, errorCode.message));
    }

    public enum ErrorCode {
        INVALID_RANGE("Invalid range"),
        TOO_MANY_ARGS("Multiple Ranges Detected");

        String message;

        ErrorCode(String message) {
            this.message = message;
        }
    }
}


