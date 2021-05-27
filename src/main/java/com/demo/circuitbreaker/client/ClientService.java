package com.demo.circuitbreaker.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class ClientService implements IClientService {

    private Random r = new Random();

    private Map<Integer, String> repository = new HashMap<>();

    private static Logger LOG = LoggerFactory.getLogger(ClientService.class);

    @PostConstruct
    public void init(){
        repository.put(1,
                "Chuck Norris was bitten by a cobra and after five days of excruciating pain... the cobra died");
        repository.put(2,
                "He who laughs last, laughs best. He who laughs at Chuck Norris, it's definitely his last laugh.");
        repository.put(3,
                "The easiest way to determine Chuck Norris's age is to cut him in half and count the rings");
        repository.put(4,
                "Chuck Norris is currently suing NBC, claiming Law and Order are trademarked names for his left and right legs");
        repository.put(5,
                "Chuck Norris doesn't dial the wrong number. You answered the wrong phone");
        repository.put(6,
                "Chuck Norris knows Victoria's secret");
        repository.put(7,
                "If Chuck Norris was a Spartan in the movie 300, the movie would be called 1");
        repository.put(8,
                "When Chuck Norris turned 18, his parents moved out");
        repository.put(9,
                "When Chuck Norris swims in the ocean, the sharks are in a steel cage");
        repository.put(10, "Chuck Norris counted to infinity… twice");
        repository.put(11, "Chuck Norris does not sleep. He waits");
        repository.put(12, "Chuck Norris can speak Braille");
        repository.put(13, "Chuck Norris once won a game of Connect Four in three moves");
        repository.put(14, "The dark is afraid of Chuck Norris");
        repository.put(15, "Chuck Norris can kill two stones with one bird");
        repository.put(16, "Chuck Norris can play the violin with a piano");
        repository.put(17, "Chuck Norris can build a snowman out of rain");
        repository.put(18, "When Chuck Norris enters a room, he doesn’t turn the lights on, he turns the dark off");
        repository.put(19, "Chuck Norris can tie his shoes with his feet");
        repository.put(20, "Chuck Norris will never have a heart attack. His heart isn't nearly foolish enough to attack him");
    }

    public <T> ResponseEntity<T> getA500Exception() {
        LOG.info("I'm going to throw a exception :) ");
        throw new RuntimeException("Circuit breaker forced exception");
    }

    public <T> ResponseEntity<T> getAChuckFact(){
        LOG.info("I'm returning a test Joke");
        Integer key = r.nextInt(20) + 1; // value should be between 1 and 20
        return (ResponseEntity<T>) ResponseEntity.ok(new ChuckFact(key, repository.get(key)));
    }

}
