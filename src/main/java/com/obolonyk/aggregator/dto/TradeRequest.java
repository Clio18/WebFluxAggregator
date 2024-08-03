package com.obolonyk.aggregator.dto;

import com.obolonyk.aggregator.domain.Ticker;
import com.obolonyk.aggregator.domain.TradeAction;

public record TradeRequest(Ticker ticker, TradeAction tradeAction, Integer quantity ) {
}
