package com.demo.circuitbreaker.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ClientControllerUnitTest {

    @Autowired
    private ClientController controller;

    @Test
    public void fallbackTest(){
        ResponseEntity<ChuckFact> responseEntity = controller.fallback(new RuntimeException());
        assertNotNull(responseEntity.getBody().getFact());
    }
}
