package com.example.adabv2.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.adabv2.Model.Discuss;

import java.util.List;

@Dao
public interface DAODiscuss {
    @Insert
    void insertDiscussMember(Discuss... discusses);

    @Query("SELECT * FROM discuss")
    List<Discuss> getAllMember();


    @Query("DELETE FROM discuss")
    void deleteAllSMember();
}
