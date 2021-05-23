package com.floplabs.vaccinecheck.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Center {
    @SerializedName("center_id")
    private int id;
    private String name;
    private String address;
    @SerializedName("district_name")
    private String districtName;
    @SerializedName("block_name")
    private String blockName;
    private long pincode;
    @SerializedName("fee_type")
    private String feeType;
    private List<Session> sessions;
    @SerializedName("vaccine_fees")
    private List<VaccineFees> vaccineFees;

    public Center(int id, String name, String address, String districtName, String blockName, long pincode, String feeType, List<Session> sessions, List<VaccineFees> vaccineFees) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.districtName = districtName;
        this.blockName = blockName;
        this.pincode = pincode;
        this.feeType = feeType;
        this.sessions = sessions;
        this.vaccineFees = vaccineFees;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public long getPincode() {
        return pincode;
    }

    public void setPincode(long pincode) {
        this.pincode = pincode;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public List<VaccineFees> getVaccineFees() {
        return vaccineFees;
    }

    public void setVaccineFees(List<VaccineFees> vaccineFees) {
        this.vaccineFees = vaccineFees;
    }

    @Override
    public String toString() {
        return "Center{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", districtName='" + districtName + '\'' +
                ", blockName='" + blockName + '\'' +
                ", pincode=" + pincode +
                ", feeType='" + feeType + '\'' +
                ", sessions=" + sessions +
                ", vaccineFees=" + vaccineFees +
                '}';
    }
}
