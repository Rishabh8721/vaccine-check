package com.floplabs.vaccinecheck.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.floplabs.vaccinecheck.aysnc.WorkChannelCheckAsyncTask;

public class UploadWorker extends Worker {
    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        int districtId = getInputData().getInt("districtId", 0);
        new WorkChannelCheckAsyncTask(this.getApplicationContext(), districtId).execute();
        return Result.success();
    }
}
