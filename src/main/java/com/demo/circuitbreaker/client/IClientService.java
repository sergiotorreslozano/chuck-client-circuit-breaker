package com.demo.circuitbreaker.client;

import org.springframework.http.ResponseEntity;

public interface IClientService {

    ChuckFact getA500Exception() ;

    ChuckFact getAChuckFact();

    ChuckFact tellAJoke();

}
