package com.floplabs.vaccinecheck.model;

import com.google.gson.annotations.SerializedName;

public class District {
    @SerializedName("district_id")
    private int id;
    @SerializedName("district_name")
    private String name;

    public District(int id, String name) {
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
