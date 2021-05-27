package com.floplabs.vaccinecheck.util;

import android.app.Activity;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;

import androidx.room.Room;

import com.floplabs.vaccinecheck.CenterSelect;
import com.floplabs.vaccinecheck.dao.NotifierChannelDao;
import com.floplabs.vaccinecheck.database.AppDatabase;
import com.floplabs.vaccinecheck.entity.NotifierChannel;

import java.lang.ref.WeakReference;

public class CreateChannelAsyncTask extends AsyncTask<Void, Void, Integer> {

    //Prevent leak
    private WeakReference<Activity> weakActivity;
    private NotifierChannel notifierChannel;

    public CreateChannelAsyncTask(Activity activity, NotifierChannel notifierChannel) {
        this.weakActivity = new WeakReference<>(activity);
        this.notifierChannel = notifierChannel;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        AppDatabase db = Room.databaseBuilder(weakActivity.get().getApplicationContext(), AppDatabase.class, "notifier-channel").build();
        NotifierChannelDao notifierChannelDao = db.notifierChannelDao();

        try {
            notifierChannelDao.createChannel(notifierChannel);
        }catch (SQLiteConstraintException exception){
            return -1;
        }
        return 0;
    }

    @Override
    protected void onPostExecute(Integer result) {
        Activity activity = weakActivity.get();
        if (activity == null) {
            return;
        }

        CenterSelect centerSelectActivity = (CenterSelect) activity;

        if (result == -1)
            centerSelectActivity.channelFailed();
        else
            centerSelectActivity.channelCreated();
    }
}
