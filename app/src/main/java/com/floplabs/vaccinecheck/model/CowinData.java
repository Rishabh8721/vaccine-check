package com.floplabs.vaccinecheck.model;

import java.util.List;

public class CowinData {
    private List<Center> centers;

    public CowinData(List<Center> centers) {
        this.centers = centers;
    }

    public List<Center> getCenters() {
        return centers;
    }

    public void setCenters(List<Center> centers) {
        this.centers = centers;
    }

    @Override
    public String toString() {
        return "CowinData{" +
                "centers=" + centers +
                '}';
    }
}
