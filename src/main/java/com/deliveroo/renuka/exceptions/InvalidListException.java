package com.deliveroo.renuka.exceptions;

import com.deliveroo.renuka.FieldType;

public class InvalidListException extends CronException {

    public InvalidListException(FieldType fieldType, ErrorCode errorCode){
        super(String.format("%s : %s", fieldType, errorCode.message));
    }

    public enum ErrorCode {
        RANGE_OUT_OF_BOUND("Invalid list range");

        String message;


        ErrorCode(String message) {
            this.message = message;
        }
    }
}

