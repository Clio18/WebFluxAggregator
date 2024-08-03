package com.obolonyk.aggregator.exception;

import reactor.core.publisher.Mono;

public class ApplicationExceptionsFactory {
    public static <T> Mono<T> notFound(Integer id){
        return Mono.error(new CustomerNotFoundException(id));
    }
    public static <T> Mono<T> validation(String message){
        return Mono.error(new InvalidTradeRequestException(message));
    }
    
    public static <T> Mono<T> missingTicker(){
        return Mono.error(new InvalidTradeRequestException("Ticker is required!"));
    }
    
    public static <T> Mono<T> missingTradeAction(){
        return Mono.error(new InvalidTradeRequestException("Trade action is required!"));
    }

    public static <T> Mono<T> invalidQuantity (){
        return Mono.error(new InvalidTradeRequestException("Quantity should be > 0!"));
    }
}
