package com.obolonyk.aggregator.exception;

public class InvalidTradeRequestException extends RuntimeException{
    public static final String MESSAGE = "";

    public InvalidTradeRequestException(String message) {
        super(message);
    }
}
