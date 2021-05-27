package com.floplabs.vaccinecheck.json;

import android.util.Log;

import com.floplabs.vaccinecheck.model.Center;
import com.floplabs.vaccinecheck.model.Session;

import java.util.ArrayList;
import java.util.List;

public class JsonFilter {

    private static final String TAG = "json_filter";

    public List<Center> getFilteredCenters(List<Center> centers, int feeType) {
        List<Center> filteredCenters = new ArrayList<>();
        switch (feeType) {
            case 0:
                return centers;
            case 1:
                for (Center center : centers) {
                    if (center.getFeeType().equals("Free"))
                        filteredCenters.add(center);
                }
                break;
            case 2:
                for (Center center : centers) {
                    if (center.getFeeType().equals("Paid"))
                        filteredCenters.add(center);
                }
                break;
            default:
                return centers;
        }
        return filteredCenters;
    }

    public List<Center> getNotifierFilteredCenters(List<Center> centers, List<Integer> centersNeeded, int feeType) {
        List<Center> filteredCenters = new ArrayList<>();
        switch (feeType) {
            case 0:
                for (Center center : centers)
                    if (centersNeeded.contains(center.getId()))
                        filteredCenters.add(center);
                return centers;
            case 1:
                for (Center center : centers) {
                    if (centersNeeded.contains(center.getId()) && center.getFeeType().equals("Free"))
                        filteredCenters.add(center);
                }
                break;
            case 2:
                for (Center center : centers) {
                    if (centersNeeded.contains(center.getId()) && center.getFeeType().equals("Paid"))
                        filteredCenters.add(center);
                }
                break;
            default:
                return centers;
        }

        Log.i(TAG, "getNotifierFilteredCenters: " + filteredCenters);
        return filteredCenters;
    }

    public List<Session> getFilteredSessions(List<Session> sessions, boolean includeBookedSlots, List<String> vaccine, boolean dose2, int age) {
        List<Session> filteredSessions = new ArrayList<>();
        for (Session session : sessions) {
            int availableCapacityDose;
            if (dose2)
                availableCapacityDose = session.getAvailableCapacityDose2();
            else
                availableCapacityDose = session.getAvailableCapacityDose1();

            if (vaccine.contains(session.getVaccine()) && session.getMinAgeLimit() <= age)
                if (!includeBookedSlots) {
                    if (availableCapacityDose > 0)
                        filteredSessions.add(session);
                } else
                    filteredSessions.add(session);
        }

        Log.i(TAG, "getFilteredSessions: " + filteredSessions);
        return filteredSessions;
    }

    public List<Session> getNotifierFilteredSessions(List<Session> sessions, List<String> vaccine, boolean dose2, int age) {
        List<Session> filteredSessions = new ArrayList<>();
        for (Session session : sessions) {
            int availableCapacityDose;
            if (dose2)
                availableCapacityDose = session.getAvailableCapacityDose2();
            else
                availableCapacityDose = session.getAvailableCapacityDose1();

            if (vaccine.contains(session.getVaccine()) && session.getMinAgeLimit() <= age)
                if (availableCapacityDose > 0)
                    filteredSessions.add(session);
        }

        Log.i(TAG, "getNotifierFilteredSessions: " + filteredSessions);
        return filteredSessions;
    }
}
