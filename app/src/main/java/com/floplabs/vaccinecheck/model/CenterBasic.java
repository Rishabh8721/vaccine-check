package com.floplabs.vaccinecheck.model;

public class CenterBasic {
    private int id;
    private String name;
    private String address;
    private String block;
    private long pincode;
    private String feeType;

    public CenterBasic(int id, String name, String address, String block, long pincode, String feeType) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.block = block;
        this.pincode = pincode;
        this.feeType = feeType;
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

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CenterBasic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", block='" + block + '\'' +
                ", pincode=" + pincode +
                ", feeType='" + feeType + '\'' +
                '}';
    }
}
