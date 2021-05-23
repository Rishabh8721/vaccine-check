package com.floplabs.vaccinecheck.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Slot implements Serializable {
    private String centerName;
    private String block;
    private long pincode;
    private Date date;
    private int availableCapacity;
    private List<String> slots;
    private String vaccine;
    private int minAgeLimit;
    private String fee;

    public Slot(String centerName, String block, long pincode, Date date, int availableCapacity, List<String> slots, String vaccine, int minAgeLimit, String fee) {
        this.centerName = centerName;
        this.block = block;
        this.pincode = pincode;
        this.date = date;
        this.availableCapacity = availableCapacity;
        this.slots = slots;
        this.vaccine = vaccine;
        this.minAgeLimit = minAgeLimit;
        this.fee = fee;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public long getPincode() {
        return pincode;
    }

    public void setPincode(long pincode) {
        this.pincode = pincode;
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

    public List<String> getSlots() {
        return slots;
    }

    public void setSlots(List<String> slots) {
        this.slots = slots;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public int getMinAgeLimit() {
        return minAgeLimit;
    }

    public void setMinAgeLimit(int minAgeLimit) {
        this.minAgeLimit = minAgeLimit;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "centerName='" + centerName + '\'' +
                ", block='" + block + '\'' +
                ", pincode=" + pincode +
                ", date=" + date +
                ", availableCapacity=" + availableCapacity +
                ", slots=" + slots +
                ", vaccine='" + vaccine + '\'' +
                ", minAgeLimit=" + minAgeLimit +
                ", fee='" + fee + '\'' +
                '}';
    }
}
