package com.floplabs.vaccinecheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.floplabs.vaccinecheck.adapter.ChannelListAdapter;
import com.floplabs.vaccinecheck.databinding.ActivityNotifierChannelBinding;
import com.floplabs.vaccinecheck.entity.NotifierChannel;
import com.floplabs.vaccinecheck.util.DeleteChannelAsyncTask;
import com.floplabs.vaccinecheck.util.FetchChannelAsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class NotifierChannelActivity extends AppCompatActivity {

    private ActivityNotifierChannelBinding binding;
    private ChannelListAdapter channelListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = binding.inflate(getLayoutInflater());
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
            binding.empty.setVisibility(View.GONE);
            binding.resultList.setLayoutManager(new LinearLayoutManager(NotifierChannelActivity.this));
            channelListAdapter = new ChannelListAdapter(notifierChannels, NotifierChannelActivity.this);
            binding.resultList.setAdapter(channelListAdapter);
        }
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