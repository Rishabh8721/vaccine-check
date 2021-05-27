package com.floplabs.vaccinecheck.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.floplabs.vaccinecheck.dao.NotifierChannelDao;
import com.floplabs.vaccinecheck.entity.NotifierChannel;

@Database(entities = {NotifierChannel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NotifierChannelDao notifierChannelDao();
}
