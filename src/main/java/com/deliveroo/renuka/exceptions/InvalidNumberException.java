package com.deliveroo.renuka.exceptions;

import com.deliveroo.renuka.FieldType;

public class InvalidNumberException extends CronException {

    public InvalidNumberException(FieldType fieldType, ErrorCode errorCode){
        super(String.format("%s : %s", fieldType, errorCode.message));
    }
    // TODO : add fieldtype
    public enum ErrorCode {
        INVALID_NUMBER("Invalid number");

        String message;

        ErrorCode(String message) {
            this.message = message;
        }
    }
}
