package com.floplabs.vaccinecheck.model;

import java.util.List;

public class CowinState {
    private List<State> states;

    public CowinState(List<State> states) {
        this.states = states;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    @Override
    public String toString() {
        return "CowinState{" +
                "states=" + states +
                '}';
    }
}
