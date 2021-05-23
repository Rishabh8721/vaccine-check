package com.floplabs.vaccinecheck.model;

import com.google.gson.annotations.SerializedName;

public class State {
    @SerializedName("state_id")
    private int id;
    @SerializedName("state_name")
    private String name;

    public State(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "District{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
