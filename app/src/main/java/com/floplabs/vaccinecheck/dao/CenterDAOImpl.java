package com.floplabs.vaccinecheck.dao;

import android.util.Log;

import com.floplabs.vaccinecheck.http.JsonTask;
import com.floplabs.vaccinecheck.json.JsonConverter;
import com.floplabs.vaccinecheck.model.Center;
import com.floplabs.vaccinecheck.model.District;
import com.floplabs.vaccinecheck.model.State;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CenterDAOImpl implements CenterDAO {

    private static final String TAG = "center_dao_impl";
    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public List<State> fetchStates() {
        String statesJson = "";
        try {
            statesJson = new JsonTask().execute("https://cdn-api.co-vin.in/api/v2/admin/location/states").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new JsonConverter().getStates(statesJson).getStates();
    }

    @Override
    public List<District> fetchDistricts(int StateId) {
        String districtsJson = "";
        try {
            districtsJson = new JsonTask().execute("https://cdn-api.co-vin.in/api/v2/admin/location/districts/" + StateId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new JsonConverter().getDistricts(districtsJson).getDistricts();
    }

    @Override
    public List<Center> fetchByDistrict(int districtId) {
        String dataJson = "";
        try {
            dataJson = new JsonTask().execute("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=" + districtId + "&date=" + dateFormat.format(new Date())).get();
            Log.d(TAG, "fetchByDistrict: " + "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=" + districtId + "&date=" + dateFormat.format(new Date()));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new JsonConverter().getCowinData(dataJson).getCenters();
    }

    @Override
    public List<Center> fetchByDistrictOnDate(int districtId, Date date) {
        String dataJson = "";
        try {
            dataJson = new JsonTask().execute("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=" + districtId + "&date=" + dateFormat.format(date)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new JsonConverter().getCowinData(dataJson).getCenters();
    }

    @Override
    public List<Center> fetchByPincode(long pincode) {
        String dataJson = "";
        try {
            dataJson = new JsonTask().execute("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=" + pincode + "&date=" + dateFormat.format(new Date())).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new JsonConverter().getCowinData(dataJson).getCenters();
    }

    @Override
    public List<Center> fetchByPincodeOnDate(long pincode, Date date) {
        String dataJson = "";
        try {
            dataJson = new JsonTask().execute("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=" + pincode + "&date=" + dateFormat.format(date)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new JsonConverter().getCowinData(dataJson).getCenters();
    }

    @Override
    public Center fetchByCenter(long centerId) {
        String dataJson = "";
        try {
            dataJson = new JsonTask().execute("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByCenter?center_id=" + centerId + "&date=" + dateFormat.format(new Date())).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new JsonConverter().getCowinCenterData(dataJson).getCenter();
    }

    @Override
    public Center fetchByCenterOnDate(long centerId, Date date) {
        String dataJson = "";
        try {
            dataJson = new JsonTask().execute("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByCenter?center_id=" + centerId + "&date=" + dateFormat.format(date)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new JsonConverter().getCowinCenterData(dataJson).getCenter();
    }
}
