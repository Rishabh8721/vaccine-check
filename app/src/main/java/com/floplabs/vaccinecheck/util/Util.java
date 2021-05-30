package com.floplabs.vaccinecheck.util;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import com.floplabs.vaccinecheck.service.NotifierJobService;

import java.util.List;

public class Util {

    public static void scheduleJob(Context context, int jobId) {
        ComponentName serviceComponent = new ComponentName(context, NotifierJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(jobId, serviceComponent);
        builder.setRequiresCharging(false);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPeriodic(15 * 60 * 1000);
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }

    public static List<JobInfo> getRunningNotifiers(Context context) {
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        return jobScheduler.getAllPendingJobs();
    }

    public static void stopNotifier(Context context, int id) {
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.cancel(id);
    }

}
