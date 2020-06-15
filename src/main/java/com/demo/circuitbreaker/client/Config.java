package com.demo.circuitbreaker.client;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.internal.InMemoryCircuitBreakerRegistry;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Config {

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Bean
    public HttpTraceRepository httpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }

    @Bean
    CircuitBreaker mycircuitBreaker (){
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("circuitBreaker");
        return circuitBreaker;
    }


    @Bean
    CircuitBreakerRegistry circuitBreakerRegistry(){
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindow(10,10 , CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(3))
                .permittedNumberOfCallsInHalfOpenState(3)
                .build();
        return CircuitBreakerRegistry.of(circuitBreakerConfig);
    }

//    @Bean
//    public TaggedCircuitBreakerMetrics taggedCircuitBreakerMetrics(){
//        MeterRegistry meterRegistry = new SimpleMeterRegistry();
//        TaggedCircuitBreakerMetrics metrics =TaggedCircuitBreakerMetrics.ofCircuitBreakerRegistry(circuitBreakerRegistry);
//        metrics.bindTo(meterRegistry);
//        return metrics;
//    }

}

