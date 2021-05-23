package com.floplabs.vaccinecheck;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.floplabs.vaccinecheck.adapter.SlotListAdapter;
import com.floplabs.vaccinecheck.databinding.ActivitySlotResultsBinding;
import com.floplabs.vaccinecheck.json.JsonSort;
import com.floplabs.vaccinecheck.model.Slot;

import java.util.List;

public class SlotResults extends AppCompatActivity {

    private static final String TAG = "slot_results";
    private ActivitySlotResultsBinding binding;
    private List<Slot> slots;
    private SlotListAdapter slotListAdapter;
    private JsonSort jsonSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        jsonSort = new JsonSort();

        slots = (List<Slot>) getIntent().getSerializableExtra("SLOTS");
        Log.i(TAG, "onCreate: " + slots);

        if (slots == null)
            binding.empty.setVisibility(View.VISIBLE);
        else if (slots.isEmpty())
            binding.empty.setVisibility(View.VISIBLE);
        else
            setSpinnerListener();
    }

    private void setSpinnerListener() {
        binding.dateSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Toast.makeText(SlotResults.this, "fetching data...", Toast.LENGTH_SHORT).show();
                slots = getSortedSlots(slots, position);

                binding.empty.setVisibility(View.GONE);
                binding.resultList.setLayoutManager(new LinearLayoutManager(SlotResults.this));
                slotListAdapter = new SlotListAdapter(slots, SlotResults.this);
                binding.resultList.setAdapter(slotListAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }

    private List<Slot> getSortedSlots(List<Slot> slots, int position) {
        switch (position) {
            case 0:
                return jsonSort.getDateSortedSlots(slots, false);
            case 1:
                return jsonSort.getDateSortedSlots(slots, true);
            case 2:
                return jsonSort.getDoseSortedSlots(slots, true);
            case 3:
                return jsonSort.getDoseSortedSlots(slots, false);
            default:
                return jsonSort.getDateSortedSlots(slots, false);
        }
    }
}