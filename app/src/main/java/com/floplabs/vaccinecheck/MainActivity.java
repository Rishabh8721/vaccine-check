package com.floplabs.vaccinecheck;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.floplabs.vaccinecheck.dao.CenterDAOImpl;
import com.floplabs.vaccinecheck.databinding.ActivityMainBinding;
import com.floplabs.vaccinecheck.json.JsonFilter;
import com.floplabs.vaccinecheck.model.Center;
import com.floplabs.vaccinecheck.model.District;
import com.floplabs.vaccinecheck.model.Session;
import com.floplabs.vaccinecheck.model.Slot;
import com.floplabs.vaccinecheck.model.State;
import com.floplabs.vaccinecheck.model.VaccineFees;
import com.floplabs.vaccinecheck.util.TinyDB;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "main_activity";
    private ActivityMainBinding binding;
    private CenterDAOImpl centerDAO;
    private JsonFilter jsonFilter;
    private int resultMode = 0; // 0-->district, 1-->pincode, 2-->center
    private List<State> states;
    private List<District> districts;
    private boolean firstDistrictStateName, firstDistrictDistrictName;

    private FirebaseAnalytics mFirebaseAnalytics;
    TinyDB tinydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        tinydb = new TinyDB(MainActivity.this);

        centerDAO = new CenterDAOImpl();
        jsonFilter = new JsonFilter();

        try{
            getStates();
        }catch (Exception e){
            Toast.makeText(this, "Can't fetch data, check network", Toast.LENGTH_SHORT).show();
            return;
        }

        setListeners();
    }

    private void getStates() {
        states = centerDAO.fetchStates();
        if (states == null)
            Toast.makeText(this, "Can't fetch data, check network", Toast.LENGTH_SHORT).show();
        else if (states.isEmpty())
            Toast.makeText(this, "Can't fetch data, check network", Toast.LENGTH_SHORT).show();
        else {
            binding.districtStateName.setAdapter(getStateSpinnerAdapter());
            firstDistrictStateName = true;
            firstDistrictDistrictName = true;
            binding.districtStateName.setSelection(tinydb.getInt("STATE_VALUE"));
        }
    }

    private void setListeners() {
        binding.districtStateName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (firstDistrictStateName)
                    firstDistrictStateName = false;
                else
                    tinydb.putInt("STATE_VALUE", position);

                districts = centerDAO.fetchDistricts(states.get(position).getId());
                binding.districtDistrictName.setAdapter(getDistrictSpinnerAdapter());
                binding.districtDistrictName.setSelection(tinydb.getInt("DISTRICT_VALUE"));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        binding.districtDistrictName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (firstDistrictDistrictName)
                    firstDistrictDistrictName = false;
                else
                    tinydb.putInt("DISTRICT_VALUE", position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        binding.resultsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.resultDestrict) {
                binding.resultLayoutCenter.setVisibility(View.GONE);
                binding.resultLayoutDistrict.setVisibility(View.VISIBLE);
                binding.resultLayoutPincode.setVisibility(View.GONE);
                resultMode = 0;
            } else if (checkedId == R.id.resultPincode) {
                binding.resultLayoutCenter.setVisibility(View.GONE);
                binding.resultLayoutDistrict.setVisibility(View.GONE);
                binding.resultLayoutPincode.setVisibility(View.VISIBLE);
                resultMode = 1;
            } else if (checkedId == R.id.resultCenter) {
                binding.resultLayoutCenter.setVisibility(View.VISIBLE);
                binding.resultLayoutDistrict.setVisibility(View.GONE);
                binding.resultLayoutPincode.setVisibility(View.GONE);
                resultMode = 2;
            }
        });

        binding.check.setOnClickListener(v -> {
            List<Center> centers = new ArrayList<>();
            if (resultMode == 0) {
                centers = centerDAO.fetchByDistrict(districts.get(binding.districtDistrictName.getSelectedItemPosition()).getId());
            } else if (resultMode == 1) {
                if (validPincode(binding.pincode.getText().toString()))
                    centers = centerDAO.fetchByPincode(Long.parseLong(binding.pincode.getText().toString()));
                else
                    return;
            } else if (resultMode == 2) {
                Toast.makeText(this, "Under development", Toast.LENGTH_SHORT).show();
                return;
            }

            int feeType = 0;
            if (binding.feeFree.isChecked())
                feeType = 1;
            else if (binding.feePaid.isChecked())
                feeType = 2;

            List<Center> feeFilteredCenters = jsonFilter.getFilteredCenters(centers, feeType);

            List<String> vaccine = new ArrayList<>();
            vaccine.add("COVISHIELD");
            vaccine.add("COVAXIN");
            vaccine.add("SPUTNIK V");

            if (!binding.vaccineCovishield.isChecked() && !binding.vaccineCovaxin.isChecked() && !binding.vaccineSputnik.isChecked())
                ;
            else {
                if (!binding.vaccineCovishield.isChecked())
                    vaccine.remove("COVISHIELD");
                if (!binding.vaccineCovaxin.isChecked())
                    vaccine.remove("COVAXIN");
                if (!binding.vaccineSputnik.isChecked())
                    vaccine.remove("SPUTNIK V");
            }

            List<Slot> slots = new ArrayList<>();

            for (Center feeFilteredCenter : feeFilteredCenters) {
                int age = 0;
                if (binding.age18.isChecked())
                    age = 18;
                else if (binding.age45.isChecked())
                    age = 45;
                List<Session> sessionFiltered = jsonFilter.getFilteredSessions(feeFilteredCenter.getSessions(), binding.bookedSlots.isChecked(), vaccine, binding.dose2.isChecked(), age);
                List<VaccineFees> vaccineFeesList = feeFilteredCenter.getVaccineFees();

                for (Session session : sessionFiltered) {
                    int availableCapacityDose;
                    if (binding.dose2.isChecked())
                        availableCapacityDose = session.getAvailableCapacityDose2();
                    else
                        availableCapacityDose = session.getAvailableCapacityDose1();

                    String fee;
                    if (vaccineFeesList == null)
                        fee = "Free";
                    else {
                        HashMap<String, String> vaccineFeesMap = new HashMap<>();
                        for (VaccineFees fees : vaccineFeesList)
                            vaccineFeesMap.put(fees.getVaccine(), fees.getFee());

                        fee = "\u20B9 " + vaccineFeesMap.get(session.getVaccine());
                    }

                    slots.add(new Slot(feeFilteredCenter.getName(), feeFilteredCenter.getBlockName(), feeFilteredCenter.getPincode(), session.getDate(), availableCapacityDose, session.getSlots(), session.getVaccine(), session.getMinAgeLimit(), fee));
                }
            }

            Log.i(TAG, "setListeners: " + slots);
            Intent i = new Intent(MainActivity.this, SlotResults.class);
            i.putExtra("SLOTS", (Serializable) slots);
            startActivity(i);
        });
    }

    private ArrayAdapter<String> getDistrictSpinnerAdapter() {
        List<String> districtNames = new ArrayList<>();
        for (District district : districts)
            districtNames.add(district.getName());
        ArrayAdapter<String> districtSpinnerAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, districtNames);
        districtSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return districtSpinnerAdapter;
    }

    private ArrayAdapter<String> getStateSpinnerAdapter() {
        List<String> stateNames = new ArrayList<>();
        for (State state : states)
            stateNames.add(state.getName());
        ArrayAdapter<String> stateSpinnerAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, stateNames);
        stateSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return stateSpinnerAdapter;
    }

    private boolean validPincode(String pincode) {
        if (pincode.isEmpty()) {
            binding.pincode.setError("Required");
            Toast.makeText(this, "Please enter pincode", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pincode.length() != 6) {
            binding.pincode.setError("Invalid");
            Toast.makeText(this, "Invalid pincode", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            Long.parseLong(pincode);
        } catch (Exception e) {
            binding.pincode.setError("Invalid");
            Toast.makeText(this, "Invalid pincode", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}