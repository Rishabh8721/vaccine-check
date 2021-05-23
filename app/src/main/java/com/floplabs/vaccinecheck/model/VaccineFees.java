package com.floplabs.vaccinecheck.model;

public class VaccineFees {
    private String vaccine;
    private String fee;

    public VaccineFees(String vaccine, String fee) {
        this.vaccine = vaccine;
        this.fee = fee;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    @Override
    public String toString() {
        return "VaccineFees{" +
                "vaccine='" + vaccine + '\'' +
                ", fee='" + fee + '\'' +
                '}';
    }
}
