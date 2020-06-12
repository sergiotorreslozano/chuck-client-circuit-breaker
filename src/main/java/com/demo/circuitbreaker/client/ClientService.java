package com.demo.circuitbreaker.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClientService implements IClientService {

    private static Logger LOG = LoggerFactory.getLogger(ClientService.class);

    public <T> ResponseEntity<T> getA500Exception() {
        LOG.info("I'm going to throw a exception :) ");
        throw new RuntimeException("Circuit breaker forced exception");
    }

    public <T> ResponseEntity<T> getAChuckFact(){
        LOG.info("I'm returning a test Joke");
        return (ResponseEntity<T>) ResponseEntity.ok(new ChuckFact(1,"test"));
    }

}
