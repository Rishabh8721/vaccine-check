package com.floplabs.vaccinecheck;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.floplabs.vaccinecheck.adapter.SlotListAdapter;
import com.floplabs.vaccinecheck.databinding.ActivitySlotResultsBinding;
import com.floplabs.vaccinecheck.model.Slot;

import java.util.List;

public class SlotResults extends AppCompatActivity {

    private static final String TAG = "slot_results";
    private ActivitySlotResultsBinding binding;
    private List<Slot> slots;
    private SlotListAdapter slotListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        slots = (List<Slot>) getIntent().getSerializableExtra("SLOTS");
        Log.i(TAG, "onCreate: " + slots);

        if (slots == null)
            binding.empty.setVisibility(View.VISIBLE);
        else if (slots.isEmpty())
            binding.empty.setVisibility(View.VISIBLE);
        else {
            binding.empty.setVisibility(View.GONE);
            binding.resultList.setLayoutManager(new LinearLayoutManager(this));
            slotListAdapter = new SlotListAdapter(slots, SlotResults.this);
            binding.resultList.setAdapter(slotListAdapter);
        }
    }
}