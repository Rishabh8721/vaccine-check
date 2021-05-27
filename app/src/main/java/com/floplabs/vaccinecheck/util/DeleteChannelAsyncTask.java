package com.floplabs.vaccinecheck.util;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

import com.floplabs.vaccinecheck.dao.NotifierChannelDao;
import com.floplabs.vaccinecheck.database.AppDatabase;

public class DeleteChannelAsyncTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private int districtId;

    public DeleteChannelAsyncTask(Context context, int districtId) {
        this.context = context;
        this.districtId = districtId;
    }

    @Override
    protected Void doInBackground(Void... params) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "notifier-channel").build();
        NotifierChannelDao notifierChannelDao = db.notifierChannelDao();
        notifierChannelDao.deleteChannelByDistrictId(districtId);
        return null;
    }

    @Override
    protected void onPostExecute(Void param) {

    }
}
