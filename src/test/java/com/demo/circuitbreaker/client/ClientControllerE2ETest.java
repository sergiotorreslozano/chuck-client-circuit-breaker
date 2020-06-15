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

    @Test
    public void circuitBreakerTest() throws InterruptedException {
        for(int i= 0 ; i<10;i++){
            makeAGoodRequest();
        }
        for(int i= 0 ; i<10;i++){
            makeABadRequest();
        }
        Thread.sleep(3000);
        for(int i= 0 ; i<10;i++){
            makeAGoodRequest();
        }
    }

    ChuckFact makeAGoodRequest(){
        return restTemplate.getForEntity("http://localhost:"+ port +"/chuckJoke", ChuckFact.class).getBody();
    }

    ChuckFact makeABadRequest(){
        return restTemplate.getForEntity("http://localhost:"+ port +"/chuckException", ChuckFact.class).getBody();
    }

}
