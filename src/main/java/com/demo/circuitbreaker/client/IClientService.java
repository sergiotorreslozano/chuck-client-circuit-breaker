package com.demo.circuitbreaker.client;

import org.springframework.http.ResponseEntity;

public interface IClientService {

    <T> ResponseEntity<T> getA500Exception() ;

    <T> ResponseEntity<T> getAChuckFact();

}
