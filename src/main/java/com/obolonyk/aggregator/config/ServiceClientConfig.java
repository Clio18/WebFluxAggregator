package com.obolonyk.aggregator.config;

import com.obolonyk.aggregator.client.CustomerServiceClient;
import com.obolonyk.aggregator.client.StockServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ServiceClientConfig {
    public static final Logger log = LoggerFactory.getLogger(ServiceClientConfig.class);

    @Bean
    public CustomerServiceClient customerServiceClient(@Value("${customer.service.url}") String baseUrl) {
        return new CustomerServiceClient(create(baseUrl));
    }

    @Bean
    public StockServiceClient stockServiceClient(@Value("${stock.service.url}") String baseUrl) {
        return new StockServiceClient(create(baseUrl));
    }

    private WebClient create(String baseUrl) {
        log.info("base url: {}", baseUrl);
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
