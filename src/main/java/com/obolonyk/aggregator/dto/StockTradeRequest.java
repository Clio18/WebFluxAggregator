package com.obolonyk.aggregator.dto;


import com.obolonyk.aggregator.domain.Ticker;
import com.obolonyk.aggregator.domain.TradeAction;

public record StockTradeRequest(Ticker ticker,
                                Integer price,
                                Integer quantity,
                                TradeAction action
) {
    public Integer getTotalPrice(){
        return price*quantity;
    }
}
