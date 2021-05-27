package com.floplabs.vaccinecheck;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.floplabs.vaccinecheck.adapter.CenterListAdapter;
import com.floplabs.vaccinecheck.dao.CenterDAOImpl;
import com.floplabs.vaccinecheck.databinding.ActivityCenterSelectBinding;
import com.floplabs.vaccinecheck.entity.NotifierChannel;
import com.floplabs.vaccinecheck.json.JsonFilter;
import com.floplabs.vaccinecheck.model.Center;
import com.floplabs.vaccinecheck.model.CenterBasic;
import com.floplabs.vaccinecheck.util.CreateChannelAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CenterSelect extends AppCompatActivity {

    private static final String TAG = "center_select";
    private ActivityCenterSelectBinding binding;
    private NotifierChannel notifierChannel;
    private CenterDAOImpl centerDAO;
    private JsonFilter jsonFilter;
    private CenterListAdapter centerListAdapter;
    private HashMap<Integer, String> centerMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        centerMap = new HashMap<>();
        centerDAO = new CenterDAOImpl();
        jsonFilter = new JsonFilter();

        notifierChannel = (NotifierChannel) getIntent().getSerializableExtra("CHANNEL");

        List<Center> centers = centerDAO.fetchByDistrict(notifierChannel.getDid());
        centers = jsonFilter.getFilteredCenters(centers, notifierChannel.getFeeType());

        if (centers.isEmpty())
            binding.empty.setVisibility(View.VISIBLE);
        else {
            List<CenterBasic> centerBasics = new ArrayList<>();

            for (Center center : centers)
                centerBasics.add(new CenterBasic(center.getId(), center.getName(), center.getAddress(), center.getBlockName(), center.getPincode(), center.getFeeType()));

            binding.empty.setVisibility(View.GONE);
            binding.centerList.setLayoutManager(new LinearLayoutManager(CenterSelect.this));
            centerListAdapter = new CenterListAdapter(centerBasics, CenterSelect.this);
            binding.centerList.setAdapter(centerListAdapter);
        }

        binding.create.setOnClickListener(v -> {
            if (centerMap.isEmpty()){
                centerMap.put(0, "All");
                notifierChannel.setCenters(centerMap);
                notifierChannel.setAllCenters(true);
            }else
                notifierChannel.setCenters(centerMap);

            Log.i(TAG, "onCreate: " + notifierChannel);
            new CreateChannelAsyncTask(this, notifierChannel).execute();
        });
    }

    public void channelCreated(){
        Toast.makeText(this, "Channel created sucessfully", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(CenterSelect.this, NotifierChannelActivity.class);
        startActivity(i);
        finish();
    }

    public void channelFailed(){
        Toast.makeText(this, "Channel already exists for same district", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(CenterSelect.this, NotifierChannelActivity.class);
        startActivity(i);
        finish();
    }

    public void insertCenter(int id, String name){
        if (!centerMap.containsKey(id))
            centerMap.put(id, name);
    }

    public void removeCenter(int id){
        centerMap.remove(id);
    }

    public boolean centerIsPresent(int id){
        return centerMap.containsKey(id);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CenterSelect.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}