package com.demo.circuitbreaker.client;

import java.io.Serializable;

public class ChuckFact implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String fact;

    public ChuckFact() {
        super();
    }

    public ChuckFact(int id, String fact) {
        this.id = id;
        this.fact = fact;
    }

    public ChuckFact(String fact){
        this.fact = fact;
    }

    public int getId() {
        return id;
    }

    public String getFact() {
        return fact;
    }

}
