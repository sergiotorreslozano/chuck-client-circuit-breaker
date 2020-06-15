# chuck-client-circuit-breaker
This is a simple Spring boot application with the circuit breaker pattern applied.

There are three different endpoints in the application:

** /chuck ** will return a Chuck joke but not protecting it with the Circuit breaker feature

** /chuckJoke ** same as before but protecting the call with the Circuit breaker feature

** /chuckException ** It will return a fallback triggering an internal exception

In order to test everything the method circuitBreakerTest should do the whole transition from CLOSE --> OPEN --> HAlF OPEN --> CLOSED 




