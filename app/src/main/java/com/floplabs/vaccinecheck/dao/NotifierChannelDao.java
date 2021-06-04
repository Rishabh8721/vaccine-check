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

    @Query("SELECT * FROM notifierchannel WHERE did = :did")
    NotifierChannel getWithId(int did);

    @Query("SELECT did FROM notifierchannel WHERE work_id = :workId")
    int getDistrictId(String workId);

    @Query("UPDATE notifierchannel SET work_id = :workId WHERE did = :did")
    void updateWorkId(String workId, int did);

    @Insert
    void createChannel(NotifierChannel notifierChannel);

    @Query("DELETE FROM notifierchannel WHERE did = :did")
    void deleteChannelByDistrictId(int did);
}
