package com.obolonyk.aggregator;

import org.junit.jupiter.api.BeforeAll;
import org.mockserver.client.MockServerClient;
import org.mockserver.configuration.ConfigurationProperties;
import org.mockserver.springtest.MockServerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(properties = {
        "customer.service.url=http://localhost:${mockServerPort}",
        "stock.service.url=http://localhost:${mockServerPort}"
})
@AutoConfigureWebTestClient
@MockServerTest
public abstract class AbstractIntegrationTest {

    // use protected because it will be extending

    // it set by @MockServerTest automatically
    protected MockServerClient mockServerClient;

    @Autowired
    protected WebTestClient client;

    @BeforeAll
    static void setUp(){
        ConfigurationProperties.disableLogging(true);
    }
}
