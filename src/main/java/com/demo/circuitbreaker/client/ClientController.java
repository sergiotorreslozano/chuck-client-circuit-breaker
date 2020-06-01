package com.demo.circuitbreaker.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
public class ClientController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CircuitBreaker circuitBreaker;

    @Value(("${chuck.service.url}"))
    private String url;

    @GetMapping("/hello")
    public String helloworld(){
        return "Hello world";
    }

    @GetMapping("/chuck")
    public ResponseEntity<ChuckFact> chuckJokeWithoutCB(){
        ResponseEntity<ChuckFact> response;
        try{
            response = restTemplate.getForEntity(URI.create(url + "/chuck"), ChuckFact.class);
        }catch (HttpStatusCodeException ex){
            response = ResponseEntity.ok(new ChuckFact("Chuck is resting"));
        }
        return response;
    }

    @GetMapping("/chuckcb")
    public ResponseEntity<ChuckFact> chuckJoke(){
        return circuitBreaker.run(
                () -> restTemplate.getForEntity(URI.create(url + "/chuck"), ChuckFact.class),
                throwable -> fallback());
    }

    public ResponseEntity<ChuckFact> fallback(){
        return ResponseEntity.ok(new ChuckFact("Chuck is taking a rest"));
    }
}


