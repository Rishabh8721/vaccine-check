package com.floplabs.vaccinecheck.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.floplabs.vaccinecheck.util.NotifierChannelTypeConverter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Entity
@TypeConverters(NotifierChannelTypeConverter.class)
public class NotifierChannel implements Serializable {
    @PrimaryKey
    private int did;

    @ColumnInfo(name = "work_id")
    private String workId;

    @ColumnInfo(name = "district_name")
    private String districtName;

    @ColumnInfo(name = "all_centers")
    private boolean allCenters;

    private HashMap<Integer, String> centers;

    private List<String> vaccines;

    @ColumnInfo(name = "second_dose")
    private boolean secondDose;

    private int age;

    @ColumnInfo(name = "fee_type")
    private int feeType;

    public NotifierChannel() {}

    @Ignore
    public NotifierChannel(int did, String workId, String districtName, boolean allCenters, HashMap<Integer, String> centers, List<String> vaccines, boolean secondDose, int age, int feeType) {
        this.did = did;
        this.workId = workId;
        this.districtName = districtName;
        this.allCenters = allCenters;
        this.centers = centers;
        this.vaccines = vaccines;
        this.secondDose = secondDose;
        this.age = age;
        this.feeType = feeType;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public List<String> getVaccines() {
        return vaccines;
    }

    public void setVaccines(List<String> vaccines) {
        this.vaccines = vaccines;
    }

    public boolean isSecondDose() {
        return secondDose;
    }

    public void setSecondDose(boolean secondDose) {
        this.secondDose = secondDose;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getFeeType() {
        return feeType;
    }

    public void setFeeType(int feeType) {
        this.feeType = feeType;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public HashMap<Integer, String> getCenters() {
        return centers;
    }

    public void setCenters(HashMap<Integer, String> centers) {
        this.centers = centers;
    }

    public boolean isAllCenters() {
        return allCenters;
    }

    public void setAllCenters(boolean allCenters) {
        this.allCenters = allCenters;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    @Override
    public String toString() {
        return "NotifierChannel{" +
                "did=" + did +
                ", workId='" + workId + '\'' +
                ", districtName='" + districtName + '\'' +
                ", allCenters=" + allCenters +
                ", centers=" + centers +
                ", vaccines=" + vaccines +
                ", secondDose=" + secondDose +
                ", age=" + age +
                ", feeType=" + feeType +
                '}';
    }
}
