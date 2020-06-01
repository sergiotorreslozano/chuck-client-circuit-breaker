package com.demo.circuitbreaker.client;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    CircuitBreaker mycircuitBreaker (){
        CircuitBreaker mycircuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        return mycircuitBreaker;
    }




}

