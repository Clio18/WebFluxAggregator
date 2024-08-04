package com.obolonyk.aggregator.controller;

import com.obolonyk.aggregator.dto.CustomerInformation;
import com.obolonyk.aggregator.dto.StockTradeRequest;
import com.obolonyk.aggregator.dto.StockTradeResponse;
import com.obolonyk.aggregator.dto.TradeRequest;
import com.obolonyk.aggregator.service.CustomerPortfolioService;
import com.obolonyk.aggregator.validator.RequestValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("customers")
public class CustomerPortfolioController {
    private final CustomerPortfolioService service;

    public CustomerPortfolioController(CustomerPortfolioService service) {
        this.service = service;
    }

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomerInfo(@PathVariable Integer customerId){
        return service.getCustomerInformation(customerId);
    }

    @PostMapping("/{customerId}/trade")
    public Mono<StockTradeResponse> getTrade(@PathVariable Integer customerId, @RequestBody Mono<TradeRequest> mono){
        return mono.transform(RequestValidator.validate())
                .flatMap(req ->  service.getStockTrade(customerId, req));
    }
}
