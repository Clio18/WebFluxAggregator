package com.obolonyk.aggregator.client;

import com.obolonyk.aggregator.dto.CustomerInformation;
import com.obolonyk.aggregator.dto.StockTradeRequest;
import com.obolonyk.aggregator.dto.StockTradeResponse;
import com.obolonyk.aggregator.exception.ApplicationExceptionsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class CustomerServiceClient {
    public static final Logger log = LoggerFactory.getLogger(CustomerServiceClient.class);

    private final WebClient client;

    public CustomerServiceClient(WebClient client) {
        this.client = client;
    }

    public Mono<CustomerInformation> getCustomerInformation(Integer id) {
        return client.get()
                .uri("/customers/{id}", id)
                .retrieve()
                .bodyToMono(CustomerInformation.class)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> ApplicationExceptionsFactory.notFound(id));
    }

    public Mono<StockTradeResponse> trade(Integer customerId, StockTradeRequest request) {
        return client.post()
                .uri("/customers/{customerId}/trade", customerId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(StockTradeResponse.class)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> ApplicationExceptionsFactory.notFound(customerId))
                .onErrorResume(WebClientResponseException.BadRequest.class, this::handleException);
    }

    private <T> Mono<T> handleException(WebClientResponseException.BadRequest exception){
        var pd = exception.getResponseBodyAs(ProblemDetail.class);
        var message = Objects.nonNull(pd) ? pd.getDetail() : exception.getMessage();
        log.error("customer service problem detail: {}", pd);
        return ApplicationExceptionsFactory.invalidTradeRequest(message);
    }

}
