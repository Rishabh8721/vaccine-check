package com.floplabs.vaccinecheck.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.floplabs.vaccinecheck.aysnc.ChannelCheckAsyncTask;

public class NotifierJobService extends JobService {
    private static final String TAG = "SyncService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "onStartJob: " + "starting channel query");
        new ChannelCheckAsyncTask(this, params.getJobId()).execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "onStopJob: " + "stopped");
        return true;
    }

}