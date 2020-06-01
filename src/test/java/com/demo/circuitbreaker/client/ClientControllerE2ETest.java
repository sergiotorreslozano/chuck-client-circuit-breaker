package com.demo.circuitbreaker.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void chuckJokeWithoutCBTest(){
        ResponseEntity<ChuckFact> fact = restTemplate.getForEntity("http://localhost:"+ port +"/chuck", ChuckFact.class);
        assertNotNull(fact.getBody().getFact());
    }

    @Test
    public void chuckJokeWithCBTest(){
        ResponseEntity<ChuckFact> fact = restTemplate.getForEntity("http://localhost:"+ port +"/chuckcb", ChuckFact.class);
        assertNotNull(fact.getBody().getFact());
    }
}
