package com.demo.circuitbreaker.client;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CircuitBreaker circuitBreaker;

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void chuckJokeWithoutCBTest(){
        ResponseEntity<ChuckFact> fact = restTemplate.getForEntity("http://localhost:"+ port +"/chuck", ChuckFact.class);
        assertNotNull(fact.getBody().getFact());
    }

    @Test
    public void chuckJokeWithCBTest(){
        ResponseEntity<ChuckFact> fact = restTemplate.getForEntity("http://localhost:"+ port +"/chuckJoke", ChuckFact.class);
        assertNotNull(fact.getBody().getFact());
    }

    /**
     * Dummy test for demo purposes, not to do in real live
     * @throws InterruptedException
     */
    @Test
    public void circuitBreakerTest() throws InterruptedException {
        for(int i= 0 ; i<10;i++){
            makeAGoodRequest();
        }
        assertTrue(circuitBreaker.getState().equals(CircuitBreaker.State.CLOSED));
        for(int i= 0 ; i<10;i++){
            makeABadRequest();
        }
        assertTrue(circuitBreaker.getState().equals(CircuitBreaker.State.OPEN));
        Thread.sleep(4000);
        makeAGoodRequest();
        assertTrue(circuitBreaker.getState().equals(CircuitBreaker.State.HALF_OPEN));
        for(int i= 0 ; i<10;i++){
            makeAGoodRequest();
        }
        assertTrue(circuitBreaker.getState().equals(CircuitBreaker.State.CLOSED));
    }

    ChuckFact makeAGoodRequest(){
        return restTemplate.getForEntity("http://localhost:"+ port +"/chuckJoke", ChuckFact.class).getBody();
    }

    ChuckFact makeABadRequest(){
        return restTemplate.getForEntity("http://localhost:"+ port +"/chuckException", ChuckFact.class).getBody();
    }

}
