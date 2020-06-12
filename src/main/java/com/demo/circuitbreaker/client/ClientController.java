package com.demo.circuitbreaker.client;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

@RestController
public class ClientController {
    private static Logger LOG = LoggerFactory.getLogger(ClientController.class);


    @Autowired
    private CircuitBreaker circuitBreaker;

    @Autowired
    private IClientService service;

    @GetMapping("/hello")
    public String helloworld(){
        return "Hello world";
    }

    @GetMapping("/chuck")
    public ResponseEntity<ChuckFact> chuckJokeWithoutCB(){
        ResponseEntity<ChuckFact> response;
        try{
            response = service.getAChuckFact();
        }catch (HttpStatusCodeException ex){
            response = ResponseEntity.ok(new ChuckFact("Chuck is resting"));
        }
        return response;
    }

    @GetMapping("/chuckcb")
    public ResponseEntity<ChuckFact> chuckJoke(){
        Supplier<ResponseEntity<ChuckFact>> decoratedSupplier = CircuitBreaker
                .decorateSupplier(circuitBreaker,
                        () -> service.getAChuckFact());
        ResponseEntity<ChuckFact> result = Try.ofSupplier(decoratedSupplier)
                .recover(throwable -> fallback(throwable)).get();
        LOG.info("State: " + circuitBreaker.getState().toString());
        return result;
    }


    @GetMapping("/chuckcb2")
    public ResponseEntity<ChuckFact> chuckJoke2() throws Exception{
        Supplier<ResponseEntity<ChuckFact>> decoratedSupplier = CircuitBreaker
                .decorateSupplier(circuitBreaker,
                        () -> service.getA500Exception());
        ResponseEntity<ChuckFact> result = Try.ofSupplier(decoratedSupplier)
                .recover(throwable -> fallback(throwable)).get();
        LOG.info("State: " + circuitBreaker.getState().toString());
        return result;
    }

    public ResponseEntity<ChuckFact> fallback(Throwable e){
        LOG.info("Fallback due to exception: " + e.getClass());
        return ResponseEntity.ok(new ChuckFact("Chuck is taking a rest"));
    }
}


