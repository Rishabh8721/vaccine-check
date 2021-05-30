package com.floplabs.vaccinecheck.aysnc;

import android.app.Activity;
import android.os.AsyncTask;

import androidx.room.Room;

import com.floplabs.vaccinecheck.NotifierChannelActivity;
import com.floplabs.vaccinecheck.dao.NotifierChannelDao;
import com.floplabs.vaccinecheck.database.AppDatabase;
import com.floplabs.vaccinecheck.entity.NotifierChannel;

import java.lang.ref.WeakReference;
import java.util.List;

public class FetchChannelAsyncTask extends AsyncTask<Void, Void, List<NotifierChannel>> {

    //Prevent leak
    private WeakReference<Activity> weakActivity;

    public FetchChannelAsyncTask(Activity activity) {
        this.weakActivity = new WeakReference<>(activity);
    }

    @Override
    protected List<NotifierChannel> doInBackground(Void... params) {
        AppDatabase db = Room.databaseBuilder(weakActivity.get().getApplicationContext(), AppDatabase.class, "notifier-channel").build();
        NotifierChannelDao notifierChannelDao = db.notifierChannelDao();

        List<NotifierChannel> notifierChannels = notifierChannelDao.getAll();
        return notifierChannels;
    }

    @Override
    protected void onPostExecute(List<NotifierChannel> notifierChannels) {
        Activity activity = weakActivity.get();
        if(activity == null) {
            return;
        }
        NotifierChannelActivity notifierChannelActivity = (NotifierChannelActivity) activity;
        notifierChannelActivity.populateChannelList(notifierChannels);

//        Notifier notifierActivity = (Notifier) activity;
//        for (NotifierChannel channel : notifierChannels)
//            notifierActivity.executeRequest(channel.getDid(), channel.getCenterIds(), channel.getVaccines(), channel.isSecondDose(), channel.getAge(), channel.getFeeType());
    }
}
