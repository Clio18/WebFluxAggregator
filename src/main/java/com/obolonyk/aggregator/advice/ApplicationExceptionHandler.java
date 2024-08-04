package com.obolonyk.aggregator.advice;

import com.obolonyk.aggregator.exception.CustomerNotFoundException;
import com.obolonyk.aggregator.exception.InvalidTradeRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.util.function.Consumer;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleException(CustomerNotFoundException exception){
        return problemDetailBuilder(HttpStatus.NOT_FOUND, exception, problem -> {
            problem.setType(URI.create("http://example.com/customer-not-found"));
            problem.setTitle("Customer not found");
        } );
    }

    @ExceptionHandler(InvalidTradeRequestException.class)
    public ProblemDetail handleException(InvalidTradeRequestException exception){
        return problemDetailBuilder(HttpStatus.BAD_REQUEST, exception, problem -> {
            problem.setType(URI.create("http://example.com/invalid-trade-request"));
            problem.setTitle("Invalid trade request");
        } );
    }

    private ProblemDetail problemDetailBuilder(HttpStatus status, Exception ex, Consumer<ProblemDetail> consumer){
        var problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        consumer.accept(problemDetail);
        return problemDetail;
    }
}
