package com.floplabs.vaccinecheck.aysnc;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

import com.floplabs.vaccinecheck.dao.NotifierChannelDao;
import com.floplabs.vaccinecheck.database.AppDatabase;

public class UpdateChannelAsyncTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private String workId;
    private int districtId;

    public UpdateChannelAsyncTask(Context context, String workId, int districtId) {
        this.context = context;
        this.workId = workId;
        this.districtId = districtId;
    }

    @Override
    protected Void doInBackground(Void... params) {
        AppDatabase db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "notifier-channel").build();
        NotifierChannelDao notifierChannelDao = db.notifierChannelDao();
        notifierChannelDao.updateWorkId(workId, districtId);
        return null;
    }

    @Override
    protected void onPostExecute(Void notifierChannels) {

    }
}
