package com.obolonyk.aggregator.dto;


import com.obolonyk.aggregator.domain.Ticker;

public record StockPriceResponse(Ticker ticker, Integer price) {
}
