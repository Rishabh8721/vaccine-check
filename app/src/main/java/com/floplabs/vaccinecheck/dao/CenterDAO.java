package com.floplabs.vaccinecheck.dao;

import com.floplabs.vaccinecheck.model.Center;
import com.floplabs.vaccinecheck.model.District;
import com.floplabs.vaccinecheck.model.State;

import java.util.Date;
import java.util.List;

public interface CenterDAO {
    List<State> fetchStates();

    List<District> fetchDistricts(int StateId);

    List<Center> fetchByDistrict(int districtId);

    List<Center> fetchByDistrictOnDate(int districtId, Date date);

    List<Center> fetchByPincode(long pincode);

    List<Center> fetchByPincodeOnDate(long pincode, Date date);

    Center fetchByCenter(long centerId);

    Center fetchByCenterOnDate(long centerId, Date date);
}
