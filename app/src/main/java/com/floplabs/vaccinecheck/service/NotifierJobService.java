package com.floplabs.vaccinecheck.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.floplabs.vaccinecheck.R;
import com.floplabs.vaccinecheck.aysnc.ChannelCheckAsyncTask;

public class NotifierJobService extends JobService {
    private static final String TAG = "SyncService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "onStartJob: " + "starting channel query");
        new ChannelCheckAsyncTask(this, params).execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "onStopJob: " + params.toString());
        createNotification();
        return true;
    }

    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "vaccine-notifier";
            String description = "Notification channel to notify vaccine slot availability";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("service-stopped", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "service-stopped")
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle("! Vaccine Service Stopped !")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            notificationManager.notify(0, builder.build());
        }
    }

}