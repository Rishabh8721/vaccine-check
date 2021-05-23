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

    public List<Session> getFilteredSessions(List<Session> sessions, boolean includeBookedSlots, List<String> vaccine, boolean dose2) {
        List<Session> filteredSessions = new ArrayList<>();

        if (dose2) {
            for (Session session : sessions)
                if (includeBookedSlots) {
                    if (vaccine.contains(session.getVaccine()))
                        filteredSessions.add(session);
                }else{
                    if (vaccine.contains(session.getVaccine()) && session.getAvailableCapacityDose2() > 0)
                        filteredSessions.add(session);
                }
        } else {
            for (Session session : sessions)
                if (includeBookedSlots) {
                    if (vaccine.contains(session.getVaccine()))
                        filteredSessions.add(session);
                }else{
                    if (vaccine.contains(session.getVaccine()) && session.getAvailableCapacityDose1() > 0)
                        filteredSessions.add(session);
                }
        }

        Log.i(TAG, "getFilteredSessions: " + filteredSessions);
        return filteredSessions;
    }
}
