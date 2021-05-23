package com.floplabs.vaccinecheck;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.floplabs.vaccinecheck.dao.CenterDAOImpl;
import com.floplabs.vaccinecheck.json.JsonFilter;
import com.floplabs.vaccinecheck.model.Center;
import com.floplabs.vaccinecheck.model.District;
import com.floplabs.vaccinecheck.model.Session;
import com.floplabs.vaccinecheck.model.State;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class CenterDaoInstrumentedTest {

    private static final String TAG = "center_dao_impl_test";
    private CenterDAOImpl centerDAO;
    private JsonFilter jsonFilter;

    @Before
    public void initialiseDao() {
        centerDAO = new CenterDAOImpl();
        jsonFilter = new JsonFilter();
    }

    @Test
    public void testFetchStates() {
        List<State> states = centerDAO.fetchStates();
        Log.d(TAG, "testFetchStates: " + states);
        assertEquals(states.get(0).getName(), "Andaman and Nicobar Islands");
    }

    @Test
    public void testFetchDistricts() {
        List<District> districts = centerDAO.fetchDistricts(34);
        Log.d(TAG, "testFetchDistricts: " + districts);
        assertEquals(districts.get(0).getName(), "Agra");
    }

    @Test
    public void testFetchByDistrict() {
        List<Center> centers = centerDAO.fetchByDistrict(651);
        Log.d(TAG, "testFetchByDistrict: " + centers);
        assertNotNull(centers);
        assertEquals(centers.get(0).getDistrictName(), "Ghaziabad");
    }

    @Test
    public void testFetchByDistrictOnDate() {
        List<Center> centers = centerDAO.fetchByDistrictOnDate(651, new Date());
        Log.d(TAG, "testFetchByDistrictOnDate: " + centers);
        assertEquals(centers.get(0).getDistrictName(), "Ghaziabad");
    }

    @Test
    public void testFetchByPincode() {
        List<Center> centers = centerDAO.fetchByPincode(201002);
        Log.d(TAG, "testFetchByPincode: " + centers);
        assertEquals(centers.get(0).getDistrictName(), "Ghaziabad");
    }

    @Test
    public void testFetchByPincodeOnDate() {
        List<Center> centers = centerDAO.fetchByPincodeOnDate(201002, new Date());
        Log.d(TAG, "testFetchByPincodeOnDate: " + centers);
        assertEquals(centers.get(0).getDistrictName(), "Ghaziabad");
    }

    @Test
    public void testFetchByCenter() {
        Center center = centerDAO.fetchByCenter(609976);
        Log.d(TAG, "testFetchByCenter: " + center);
        assertEquals(center.getDistrictName(), "Gautam Buddha Nagar");
    }

    @Test
    public void testFetchByCenterOnDate() {
        Center center = centerDAO.fetchByCenterOnDate(609976, new Date());
        Log.d(TAG, "testFetchByCenterOnDate: " + center);
        assertEquals(center.getDistrictName(), "Gautam Buddha Nagar");
    }

    @Test
    public void testGetFilteredCenters() {
        List<Center> centers = centerDAO.fetchByDistrict(650);
        assertNotNull(centers);
        assertEquals(centers.get(0).getDistrictName(), "Gautam Buddha Nagar");

        List<Center> freeCenters = jsonFilter.getFilteredCenters(centers, 1);
        Log.d(TAG, "testGetFilteredCenters: freeCenters: " + freeCenters);
        assertNotNull(freeCenters);
        for (Center freeCenter : freeCenters)
            if (!freeCenter.getFeeType().equals("Free"))
                fail();

        List<Center> paidCenters = jsonFilter.getFilteredCenters(centers, 2);
        Log.d(TAG, "testGetFilteredCenters: paidCenters: " + paidCenters);
        assertNotNull(paidCenters);
        for (Center paidCenter : paidCenters)
            if (!paidCenter.getFeeType().equals("Paid"))
                fail();
    }

    @Test
    public void testGetFilteredSessions() {
        Center center = centerDAO.fetchByCenter(609976);
        assertEquals(center.getDistrictName(), "Gautam Buddha Nagar");

        List<String> vaccine = new ArrayList<>();
        vaccine.add("COVISHIELD");

        List<Session> covishieldDose1Sessions = jsonFilter.getFilteredSessions(center.getSessions(), false, vaccine, false, 45); // Covishield & dose1
        Log.d(TAG, "testGetFilteredSessions: covishieldDose1Sessions: " + covishieldDose1Sessions);
        assertNotNull(covishieldDose1Sessions);
        for (Session covishieldDose1Session : covishieldDose1Sessions)
            if (!covishieldDose1Session.getVaccine().equals("COVISHIELD") || covishieldDose1Session.getAvailableCapacityDose1() == 0)
                fail();

        vaccine.add("COVAXIN");
        List<Session> dose2Sessions = jsonFilter.getFilteredSessions(center.getSessions(), false, vaccine, true,45); // dose2
        Log.d(TAG, "testGetFilteredSessions: dose2Sessions: " + dose2Sessions);
        assertNotNull(dose2Sessions);
        for (Session dose2Session : dose2Sessions)
            if (dose2Session.getAvailableCapacityDose2() == 0)
                fail();
    }
}