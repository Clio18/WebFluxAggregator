package com.obolonyk.aggregator.dto;

import com.obolonyk.aggregator.domain.Ticker;

import java.time.LocalDateTime;

public record PriceUpdate(Ticker ticker, Integer price, LocalDateTime time) {
}
