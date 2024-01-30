package com.demo.circuitbreaker.client;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
            response = ResponseEntity.ok(service.getAChuckFact());
        }catch (HttpStatusCodeException ex){
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ChuckFact("Chuck is taking a rest"));
        }
        return response;
    }

    @GetMapping("/chuckJoke")
    public ResponseEntity<ChuckFact> chuckJoke(){
        Supplier<ChuckFact> decoratedSupplier = CircuitBreaker
                .decorateSupplier(circuitBreaker,
                        () -> service.getAChuckFact());
        ResponseEntity<ChuckFact> result = ResponseEntity
                .status(HttpStatus.OK)
                .body((Try.ofSupplier(decoratedSupplier)).recover(throwable -> fallback(throwable)).get());
        LOG.info("State: " + circuitBreaker.getState().toString());
        return result;
    }

    ChuckFact fallback(Throwable e){
        LOG.info("Fallback due to exception: " + e.getClass());
        return new ChuckFact("Chuck is taking a rest");
    }

    @GetMapping("/chuckException")
    public ResponseEntity<ChuckFact> chuckJokeException() {
        Supplier<ChuckFact> decoratedSupplier = CircuitBreaker
                .decorateSupplier(circuitBreaker,
                        () -> service.getA500Exception());
        ResponseEntity<ChuckFact> result = ResponseEntity.status(HttpStatus.OK)
                .body(Try.ofSupplier(decoratedSupplier).recover(throwable -> fallback(throwable)).get());
        LOG.info("State: " + circuitBreaker.getState().toString());
        return result;
    }

    @GetMapping ("/joke")
    public ResponseEntity<ChuckFact> joke(){
        ResponseEntity<ChuckFact> response = ResponseEntity.ok(new ChuckFact());
        return response;
    }


}


