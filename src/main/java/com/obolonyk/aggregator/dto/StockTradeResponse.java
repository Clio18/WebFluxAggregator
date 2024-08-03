package com.obolonyk.aggregator.dto;


import com.obolonyk.aggregator.domain.Ticker;
import com.obolonyk.aggregator.domain.TradeAction;

public record StockTradeResponse(Integer customerId,
                                 Ticker ticker,
                                 Integer price,
                                 Integer quantity,
                                 TradeAction action,
                                 Integer totalPrice,
                                 Integer balance
) {
}
