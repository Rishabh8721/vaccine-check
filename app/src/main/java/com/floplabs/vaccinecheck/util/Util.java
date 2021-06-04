package com.floplabs.vaccinecheck.util;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.floplabs.vaccinecheck.aysnc.UpdateChannelAsyncTask;
import com.floplabs.vaccinecheck.service.NotifierJobService;
import com.floplabs.vaccinecheck.worker.UploadWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Util {

    /* ----------------------------------------- JobScheduler ------------------------------------------------------------*/
    public static void scheduleJob(Context context, int jobId) {
        ComponentName serviceComponent = new ComponentName(context, NotifierJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(jobId, serviceComponent);
        builder.setPeriodic(15 * 60 * 1000);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);
        builder.setRequiresDeviceIdle(false);
        builder.setRequiresCharging(false);
        builder.setPersisted(true);
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

    /* ----------------------------------------- WorkManager ------------------------------------------------------------*/
    public static void enqueueWorkRequest(Context context, int jobId) {
        Data notifierData = new Data.Builder()
                .putInt("districtId", jobId)
                .build();
        PeriodicWorkRequest saveRequest = new PeriodicWorkRequest.Builder(UploadWorker.class, 15, TimeUnit.MINUTES).setInputData(notifierData).addTag("notifier").build();
        new UpdateChannelAsyncTask(context, saveRequest.getId().toString(), jobId).execute();
        WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                jobId+"",
                ExistingPeriodicWorkPolicy.KEEP,
                saveRequest);
    }

    public static List<WorkInfo> getActiveWorkInfo(Context context) throws ExecutionException, InterruptedException {
        List<WorkInfo> workInfos;

        workInfos = WorkManager.getInstance(context).getWorkInfosByTag("notifier").get();
        if(workInfos == null)
            return new ArrayList<>();
        else
            return workInfos;
    }

    public static void stopWork(Context context, int id) {
        WorkManager workManager = WorkManager.getInstance(context);
        workManager.cancelUniqueWork(id+"");
    }
}
