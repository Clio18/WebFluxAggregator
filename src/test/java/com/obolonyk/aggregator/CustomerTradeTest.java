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

        //mock stock-service price response
        var responseBody = resourceToString("stock-service/stock-price-200.json");
        mockServerClient.when(HttpRequest.request("/stock/GOOGLE"))
                .respond(
                        HttpResponse.response(responseBody)
                                .withStatusCode(200)

                                //NB: media type
                                .withContentType(MediaType.APPLICATION_JSON)
                );

        var responseBodyTrade = resourceToString("customer-service/customer-trade-200.json");
        mockServerClient.when(
                HttpRequest.request("/customers/1/trade")
                        .withMethod("POST")
                        .withBody(RegexBody.regex(".*\"price\":110.*"))
        )
                .respond(
                        HttpResponse.response(responseBodyTrade)
                                .withStatusCode(200)
                                .withContentType(MediaType.APPLICATION_JSON)
                );


        var tradeReq = new TradeRequest(Ticker.GOOGLE, TradeAction.BUY, 2);

        postTradeReq(tradeReq, HttpStatus.OK);

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
