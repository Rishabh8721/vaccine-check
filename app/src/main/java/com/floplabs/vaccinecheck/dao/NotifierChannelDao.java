package com.floplabs.vaccinecheck.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.floplabs.vaccinecheck.entity.NotifierChannel;

import java.util.List;

@Dao
public interface NotifierChannelDao {
    @Query("SELECT * FROM notifierchannel")
    List<NotifierChannel> getAll();

    @Insert
    void createChannel(NotifierChannel notifierChannel);

    @Query("DELETE FROM notifierchannel WHERE did = :did")
    void deleteChannelByDistrictId(int did);
}
