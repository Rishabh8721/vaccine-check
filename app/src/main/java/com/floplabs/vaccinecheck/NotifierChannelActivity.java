package com.floplabs.vaccinecheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.WorkInfo;

import com.floplabs.vaccinecheck.adapter.ChannelListAdapter;
import com.floplabs.vaccinecheck.aysnc.DeleteChannelAsyncTask;
import com.floplabs.vaccinecheck.aysnc.FetchChannelAsyncTask;
import com.floplabs.vaccinecheck.databinding.ActivityNotifierChannelBinding;
import com.floplabs.vaccinecheck.entity.NotifierChannel;
import com.floplabs.vaccinecheck.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NotifierChannelActivity extends AppCompatActivity {

    private static final String TAG = "notifier_channel_activity";
    private ActivityNotifierChannelBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotifierChannelBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

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
            List<String> activeChannelIds = new ArrayList<>();

//            List<JobInfo> activeJobs = Util.getRunningNotifiers(this);
//            for (JobInfo activeJob : activeJobs)
//                activeChannelIds.add(activeJob.getId());

            try {
                List<WorkInfo> activeJobs = Util.getActiveWorkInfo(this);

                for (WorkInfo workInfo : activeJobs) {
//                    AppDatabase db = Room.databaseBuilder(NotifierChannelActivity.this, AppDatabase.class, "notifier-channel").build();
//                    NotifierChannelDao notifierChannelDao = db.notifierChannelDao();
//                    int districtId = notifierChannelDao.getDistrictId(workInfo.getId().toString());
                    activeChannelIds.add(workInfo.getId().toString());
//                    Log.i(TAG, "populateChannelList: " + districtId);
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            binding.empty.setVisibility(View.GONE);
            binding.resultList.setLayoutManager(new LinearLayoutManager(NotifierChannelActivity.this));
            ChannelListAdapter channelListAdapter = new ChannelListAdapter(notifierChannels, activeChannelIds, NotifierChannelActivity.this);
            binding.resultList.setAdapter(channelListAdapter);
        }
    }

    public void startChannel(NotifierChannel notifierChannel){
//        Util.scheduleJob(this, notifierChannel.getDid());
        Util.enqueueWorkRequest(this, notifierChannel.getDid());
        Toast.makeText(this, "Channel started successfully", Toast.LENGTH_SHORT).show();
        binding.resultList.setAdapter(null);
        loadChannelFromLocal();
    }

    public void stopChannel(int id){
//        Util.stopNotifier(this, id);
        Util.stopWork(this, id);
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
}