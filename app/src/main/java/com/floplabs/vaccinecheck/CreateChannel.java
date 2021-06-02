package com.floplabs.vaccinecheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.floplabs.vaccinecheck.dao.CenterDAOImpl;
import com.floplabs.vaccinecheck.databinding.ActivityNotifierBinding;
import com.floplabs.vaccinecheck.entity.NotifierChannel;
import com.floplabs.vaccinecheck.model.District;
import com.floplabs.vaccinecheck.model.State;

import java.util.ArrayList;
import java.util.List;

public class CreateChannel extends AppCompatActivity {

    private ActivityNotifierBinding binding;
    private List<State> states;
    private List<District> districts;
    private CenterDAOImpl centerDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotifierBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        centerDAO = new CenterDAOImpl();

        try {
            getStates();
        } catch (Exception e) {
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
        else
            binding.districtStateName.setAdapter(getStateSpinnerAdapter());
    }

    private void setListeners() {
        binding.proceed.setOnClickListener(v -> {
            int did = districts.get(binding.districtDistrictName.getSelectedItemPosition()).getId();
            String districtName = districts.get(binding.districtDistrictName.getSelectedItemPosition()).getName();

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

            int age = 0;
            if (binding.age18.isChecked())
                age = 18;
            else if (binding.age45.isChecked())
                age = 45;

            int fee = 0;
            if (binding.feeFree.isChecked())
                fee = 1;
            else if (binding.feePaid.isChecked())
                fee = 2;

            NotifierChannel notifierChannel = new NotifierChannel(did, districtName, false, null, vaccine, binding.dose2.isChecked(), age, fee);
            Intent i = new Intent(CreateChannel.this, CenterSelect.class);
            i.putExtra("CHANNEL", notifierChannel);
            startActivity(i);
            finish();
        });

        binding.districtStateName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                districts = centerDAO.fetchDistricts(states.get(position).getId());
                binding.districtDistrictName.setAdapter(getDistrictSpinnerAdapter());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private ArrayAdapter<String> getDistrictSpinnerAdapter() {
        List<String> districtNames = new ArrayList<>();
        for (District district : districts)
            districtNames.add(district.getName());
        ArrayAdapter<String> districtSpinnerAdapter = new ArrayAdapter<>(CreateChannel.this, android.R.layout.simple_spinner_item, districtNames);
        districtSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return districtSpinnerAdapter;
    }

    private ArrayAdapter<String> getStateSpinnerAdapter() {
        List<String> stateNames = new ArrayList<>();
        for (State state : states)
            stateNames.add(state.getName());
        ArrayAdapter<String> stateSpinnerAdapter = new ArrayAdapter<>(CreateChannel.this, android.R.layout.simple_spinner_item, stateNames);
        stateSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return stateSpinnerAdapter;
    }
}