package com.obolonyk.aggregator.exception;

public class CustomerNotFoundException extends RuntimeException{
    public static final String MESSAGE = "Customer with ID %d not found";

    public CustomerNotFoundException(Integer id) {
        super(MESSAGE.formatted(id));
    }
}
