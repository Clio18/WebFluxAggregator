package com.obolonyk.aggregator;

import com.obolonyk.aggregator.domain.Ticker;
import com.obolonyk.aggregator.domain.TradeAction;
import com.obolonyk.aggregator.dto.TradeRequest;
import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.mockserver.model.RegexBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

public class CustomerTradeTest extends AbstractIntegrationTest {
    public static final Logger log = LoggerFactory.getLogger(CustomerTradeTest.class);

    @Test
    public void tradeSuccess(){
        mockCustomerTrade("customer-service/customer-trade-200.json", 200);

        var tradeReq = new TradeRequest(Ticker.GOOGLE, TradeAction.BUY, 2);
        postTradeReq(tradeReq, HttpStatus.OK)
                //{"customerId":1,"ticker":"GOOGLE","price":110,"quantity":2,"action":"BUY","totalPrice":220,"balance":9780}
                .jsonPath("$.balance").isEqualTo(9780)
                .jsonPath("$.totalPrice").isEqualTo(220)
                .jsonPath("$.action").isEqualTo("BUY")
                .jsonPath("$.quantity").isEqualTo(2)
                .jsonPath("$.price").isEqualTo(110)
                .jsonPath("$.ticker").isEqualTo("GOOGLE");

    }

    @Test
    public void tradeNotSuccess(){
        mockCustomerTrade("customer-service/customer-trade-400.json", 400);

        var tradeReq = new TradeRequest(Ticker.GOOGLE, TradeAction.BUY, 2);
        postTradeReq(tradeReq, HttpStatus.BAD_REQUEST)
                //{"customerId":1,"ticker":"GOOGLE","price":110,"quantity":2,"action":"BUY","totalPrice":220,"balance":9780}
                .jsonPath("$.detail").isEqualTo("Customer ID 1 does not have enough shares to complete transaction");

    }

    private void mockCustomerTrade(String path, int responseCode){
        //mock stock-service price response
        var responseBody = resourceToString("stock-service/stock-price-200.json");
        mockServerClient.when(HttpRequest.request("/stock/GOOGLE"))
                .respond(
                        HttpResponse.response(responseBody)
                                .withStatusCode(200)

                                //NB: media type
                                .withContentType(MediaType.APPLICATION_JSON)
                );

        //mock customer-service trade response

        var responseBodyTrade = resourceToString(path);
        mockServerClient.when(
                        HttpRequest.request("/customers/1/trade")
                                .withMethod("POST")
                                .withBody(RegexBody.regex(".*\"price\":110.*"))
                )
                .respond(
                        HttpResponse.response(responseBodyTrade)
                                .withStatusCode(responseCode)
                                .withContentType(MediaType.APPLICATION_JSON)
                );
    }

    private WebTestClient.BodyContentSpec postTradeReq(TradeRequest tradeRequest, HttpStatus expectedStatus) {
        return client.post()
                .uri("/customers/1/trade")
                .bodyValue(tradeRequest)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectBody()
                .consumeWith(b -> log.info("{}", new String(Objects.requireNonNull(b.getResponseBody()))));
    }

}
