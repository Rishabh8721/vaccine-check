package com.floplabs.vaccinecheck.model;

import java.util.List;

public class CowinDistrict {
    private List<District> districts;

    public CowinDistrict(List<District> districts) {
        this.districts = districts;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    @Override
    public String toString() {
        return "CowinDistrict{" +
                "districts=" + districts +
                '}';
    }
}
