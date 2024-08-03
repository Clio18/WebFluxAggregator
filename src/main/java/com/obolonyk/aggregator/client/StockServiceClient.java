package com.obolonyk.aggregator.client;

import com.obolonyk.aggregator.domain.Ticker;
import com.obolonyk.aggregator.dto.StockPriceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class StockServiceClient {
    public static final Logger log = LoggerFactory.getLogger(StockServiceClient.class);
    private final WebClient client;

    public StockServiceClient(WebClient client) {
        this.client = client;
    }

    public Mono<StockPriceResponse> getStockPrice(Ticker ticker) {
        return client.get()
                .uri("/stock/{ticker}", ticker)
                .retrieve()
                .bodyToMono(StockPriceResponse.class);
    }

    public Flux<StockPriceResponse> getPriceUpdate() {
        return client.get()
                .uri("/stock/price-stream")
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(StockPriceResponse.class); 

    }
}

