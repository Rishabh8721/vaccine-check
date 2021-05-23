package com.floplabs.vaccinecheck.model;

import com.google.gson.annotations.SerializedName;

public class CowinCenterData {
    @SerializedName("centers")
    private Center center;

    public CowinCenterData(Center center) {
        this.center = center;
    }

    public Center getCenter() {
        return center;
    }

    public void setCenter(Center center) {
        this.center = center;
    }

    @Override
    public String toString() {
        return "CowinCenterData{" +
                "center=" + center +
                '}';
    }
}
