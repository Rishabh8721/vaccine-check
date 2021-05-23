package com.floplabs.vaccinecheck.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Session {
    @SerializedName("session_id")
    private String id;
    private Date date;
    @SerializedName("available_capacity")
    private int availableCapacity;
    @SerializedName("min_age_limit")
    private int minAgeLimit;
    private String vaccine;
    private List<String> slots;
    @SerializedName("available_capacity_dose1")
    private int availableCapacityDose1;
    @SerializedName("available_capacity_dose2")
    private int availableCapacityDose2;

    public Session(String id, Date date, int availableCapacity, int minAgeLimit, String vaccine, List<String> slots, int availableCapacityDose1, int availableCapacityDose2) {
        this.id = id;
        this.date = date;
        this.availableCapacity = availableCapacity;
        this.minAgeLimit = minAgeLimit;
        this.vaccine = vaccine;
        this.slots = slots;
        this.availableCapacityDose1 = availableCapacityDose1;
        this.availableCapacityDose2 = availableCapacityDose2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAvailableCapacity() {
        return availableCapacity;
    }

    public void setAvailableCapacity(int availableCapacity) {
        this.availableCapacity = availableCapacity;
    }

    public int getMinAgeLimit() {
        return minAgeLimit;
    }

    public void setMinAgeLimit(int minAgeLimit) {
        this.minAgeLimit = minAgeLimit;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public List<String> getSlots() {
        return slots;
    }

    public void setSlots(List<String> slots) {
        this.slots = slots;
    }

    public int getAvailableCapacityDose1() {
        return availableCapacityDose1;
    }

    public void setAvailableCapacityDose1(int availableCapacityDose1) {
        this.availableCapacityDose1 = availableCapacityDose1;
    }

    public int getAvailableCapacityDose2() {
        return availableCapacityDose2;
    }

    public void setAvailableCapacityDose2(int availableCapacityDose2) {
        this.availableCapacityDose2 = availableCapacityDose2;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", availableCapacity=" + availableCapacity +
                ", minAgeLimit=" + minAgeLimit +
                ", vaccine='" + vaccine + '\'' +
                ", slots=" + slots +
                ", availableCapacityDose1=" + availableCapacityDose1 +
                ", availableCapacityDose2=" + availableCapacityDose2 +
                '}';
    }
}
