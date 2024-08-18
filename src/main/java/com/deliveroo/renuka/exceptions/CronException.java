package com.deliveroo.renuka.exceptions;

public class CronException extends Exception{
    public CronException(String message) {
        super(message);
    }

    public CronException(ErrorCode errorCode) {
        super(String.format("%s", errorCode.message));
    }

    public enum ErrorCode {
        NULL_INPUT("Cron expression is NULL"),
        EMPTY_INPUT("Cron expression is Empty"),
        INSUFFICIENT_PARAMS("Cron expression has missing fields");

        String message;


        ErrorCode(String message) {
            this.message = message;
        }
    }
}
