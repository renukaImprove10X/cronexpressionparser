package com.deliveroo.renuka.exceptions;

import com.deliveroo.renuka.FieldType;

public class InvalidStepException extends CronException {

    public InvalidStepException(FieldType fieldType, ErrorCode errorCode){
        super(String.format("%s : %s", fieldType, errorCode.message));
    }

    public enum ErrorCode {
        INVALID_STEP("Invalid step");

        String message;

        ErrorCode(String message) {
            this.message = message;
        }
    }
}
