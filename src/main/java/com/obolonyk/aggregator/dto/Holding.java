package com.obolonyk.aggregator.dto;


import com.obolonyk.aggregator.domain.Ticker;

public record Holding(Ticker ticker,
                      Integer quantity
) {
}
