# chuck-client-circuit-breaker
This is a simple Spring boot application with the circuit breaker pattern applied.

There are three different endpoints in the application:

** /chuck ** will return a Chuck joke but not protecting it with the Circuit breaker feature

** /chuckJoke ** same as before but protecting the call with the Circuit breaker feature

** /chuckException ** It will return a fallback triggering an internal exception

In order to test everything the method circuitBreakerTest  should do the whole transition from CLOSE --> OPEN --> HAlF OPEN --> CLOSED 


To run the application:

0- Pre-requisites
```
You'll need Java 11 and Maven 3 installed
```

1- To run the test ( by default in a random port)
```
mvn clean test
```

2- To run the application (by default in port 8080)
 ```
mvn spring-boot:run
```

Actuator runs in port 9090 in case you want to take a look to the metrics

