package com.floplabs.vaccinecheck;

import android.app.job.JobInfo;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.floplabs.vaccinecheck.adapter.ChannelListAdapter;
import com.floplabs.vaccinecheck.dao.CenterDAOImpl;
import com.floplabs.vaccinecheck.databinding.ActivityNotifierChannelBinding;
import com.floplabs.vaccinecheck.entity.NotifierChannel;
import com.floplabs.vaccinecheck.json.JsonFilter;
import com.floplabs.vaccinecheck.model.Center;
import com.floplabs.vaccinecheck.model.Session;
import com.floplabs.vaccinecheck.model.Slot;
import com.floplabs.vaccinecheck.model.VaccineFees;
import com.floplabs.vaccinecheck.aysnc.DeleteChannelAsyncTask;
import com.floplabs.vaccinecheck.aysnc.FetchChannelAsyncTask;
import com.floplabs.vaccinecheck.util.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NotifierChannelActivity extends AppCompatActivity {

    private ActivityNotifierChannelBinding binding;
    private ChannelListAdapter channelListAdapter;
    private CenterDAOImpl centerDAO;
    private JsonFilter jsonFilter;
    private List<Integer> activeChannelIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        centerDAO = new CenterDAOImpl();
        jsonFilter = new JsonFilter();
        loadChannelFromLocal();

        binding.add.setOnClickListener(v -> {
            Intent i = new Intent(NotifierChannelActivity.this, CreateChannel.class);
            startActivity(i);
        });
    }

    private void loadChannelFromLocal() {
        new FetchChannelAsyncTask(this).execute();
    }

    public void populateChannelList(List<NotifierChannel> notifierChannels){
        if (notifierChannels == null)
            binding.empty.setVisibility(View.VISIBLE);
        else if (notifierChannels.isEmpty())
            binding.empty.setVisibility(View.VISIBLE);
        else {
            activeChannelIds = new ArrayList<>();
            List<JobInfo> activeJobs = Util.getRunningNotifiers(this);
            for (JobInfo activeJob : activeJobs)
                activeChannelIds.add(activeJob.getId());
            binding.empty.setVisibility(View.GONE);
            binding.resultList.setLayoutManager(new LinearLayoutManager(NotifierChannelActivity.this));
            channelListAdapter = new ChannelListAdapter(notifierChannels, activeChannelIds, NotifierChannelActivity.this);
            binding.resultList.setAdapter(channelListAdapter);
        }
    }

    public void startChannel(NotifierChannel notifierChannel){
//        Toast.makeText(this, "Starting channel", Toast.LENGTH_SHORT).show();
//        List<Integer> centerIds = new ArrayList<>(notifierChannel.getCenters().keySet());
//        executeRequest(notifierChannel.getDid(), centerIds, notifierChannel.getVaccines(), notifierChannel.isSecondDose(), notifierChannel.getAge(), notifierChannel.getFeeType());

        Util.scheduleJob(this, notifierChannel.getDid());
        Toast.makeText(this, "Channel started successfully", Toast.LENGTH_SHORT).show();
        binding.resultList.setAdapter(null);
        loadChannelFromLocal();
    }

    public void stopChannel(int id){
        Util.stopNotifier(this, id);
        Toast.makeText(this, "Channel stopped successfully", Toast.LENGTH_SHORT).show();
        binding.resultList.setAdapter(null);
        loadChannelFromLocal();
    }

    public void deleteFromLocal(int did) {
        try {
            new DeleteChannelAsyncTask(this, did).execute().get();
            Toast.makeText(this, "Channel deleted successfully", Toast.LENGTH_SHORT).show();
            binding.resultList.setAdapter(null);
            loadChannelFromLocal();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void executeRequest(int did, List<Integer> centerIds, List<String> vaccines, boolean secondDose, int age, int feeType) {
        List<Slot> slots = new ArrayList<>();
        List<Center> centers = jsonFilter.getNotifierFilteredCenters(centerDAO.fetchByDistrict(did), centerIds, feeType);
        for (Center center : centers) {
            List<Session> filteredSesisons = jsonFilter.getNotifierFilteredSessions(center.getSessions(), vaccines, secondDose, age);
            for (Session session : filteredSesisons) {
                String fee;
                List<VaccineFees> vaccineFeesList = center.getVaccineFees();
                if (vaccineFeesList == null)
                    fee = "Free";
                else {
                    HashMap<String, String> vaccineFeesMap = new HashMap<>();
                    for (VaccineFees fees : vaccineFeesList)
                        vaccineFeesMap.put(fees.getVaccine(), fees.getFee());

                    fee = "\u20B9 " + vaccineFeesMap.get(session.getVaccine());
                }

                slots.add(new Slot(center.getName(), center.getBlockName(), center.getPincode(), session.getDate(), session.getAvailableCapacityDose1(), session.getSlots(), session.getVaccine(), session.getMinAgeLimit(), fee));
            }
        }

        if (!slots.isEmpty()){
            Toast.makeText(NotifierChannelActivity.this, "Hurry! Slots available", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(NotifierChannelActivity.this, SlotResults.class);
            i.putExtra("SLOTS", (Serializable) slots);
            NotifierChannelActivity.this.startActivity(i);
        }else
            Toast.makeText(NotifierChannelActivity.this, "Sorry, No slots available", Toast.LENGTH_SHORT).show();
    }
}