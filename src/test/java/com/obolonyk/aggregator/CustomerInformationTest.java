package com.obolonyk.aggregator;

import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

public class CustomerInformationTest extends AbstractIntegrationTest {

    public static final Logger log = LoggerFactory.getLogger(CustomerInformationTest.class);

    @Test
    public void customerInformation() {
        String relativePath = "customer-service/customer-info-200.json";
        int statusCode = 200;
        mockCustomerInfo(relativePath, statusCode);

        getCustomerInfo(HttpStatus.OK)
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("sam")
                .jsonPath("$.balance").isEqualTo(1000)
                .jsonPath("$.holdings[0].ticker").isEqualTo("GOOGLE")
                .jsonPath("$.holdings[0].quantity").isEqualTo(2);
    }

    @Test
    public void customerNotFound() {
        String relativePath = "customer-service/customer-info-404.json";
        int statusCode = 404;
        mockCustomerInfo(relativePath, statusCode);

        getCustomerInfo(HttpStatus.NOT_FOUND)
                .jsonPath("$.detail").isEqualTo("Customer with ID 1 not found")
                .jsonPath("$.title").isNotEmpty();
    }

    private void mockCustomerInfo(String path, int responseCode ){
        var responseBody = resourceToString(path);
        mockServerClient.when(HttpRequest.request("/customers/1"))
                .respond(
                        HttpResponse.response(responseBody)
                                .withStatusCode(responseCode)
                                .withContentType(MediaType.APPLICATION_JSON  )
                );
    }

    private WebTestClient.BodyContentSpec getCustomerInfo(HttpStatus expectedStatus){
         return client.get()
                 .uri("/customers/1")
                 .exchange()
                 .expectStatus().isEqualTo(expectedStatus)
                 .expectBody()
                 .consumeWith(b -> log.info("{}", new String(Objects.requireNonNull(b.getResponseBody()))));
    }


}
