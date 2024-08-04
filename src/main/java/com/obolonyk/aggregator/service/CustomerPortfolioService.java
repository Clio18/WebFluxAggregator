package com.obolonyk.aggregator.service;

import com.obolonyk.aggregator.client.CustomerServiceClient;
import com.obolonyk.aggregator.client.StockServiceClient;
import com.obolonyk.aggregator.dto.CustomerInformation;
import com.obolonyk.aggregator.dto.StockPriceResponse;
import com.obolonyk.aggregator.dto.StockTradeRequest;
import com.obolonyk.aggregator.dto.StockTradeResponse;
import com.obolonyk.aggregator.dto.TradeRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerPortfolioService {
    private final StockServiceClient stockServiceClient;
    private final CustomerServiceClient customerServiceClient;

    public CustomerPortfolioService(StockServiceClient stockServiceClient, CustomerServiceClient customerServiceClient) {
        this.stockServiceClient = stockServiceClient;
        this.customerServiceClient = customerServiceClient;
    }

    public Mono<CustomerInformation> getCustomerInformation(Integer customerId){
        return customerServiceClient.getCustomerInformation(customerId);
    }

    public Mono<StockTradeResponse> getStockTrade(Integer customerId, TradeRequest request){
       return stockServiceClient.getStockPrice(request.ticker())
               .map(StockPriceResponse::price)
               .map(price -> toStockTradeRequest(request, price))
               .flatMap(req -> customerServiceClient.trade(customerId, req));
    }

    private StockTradeRequest toStockTradeRequest(TradeRequest request, Integer price){
        return new StockTradeRequest(request.ticker(), price, request.quantity(), request.tradeAction());
    }

}
