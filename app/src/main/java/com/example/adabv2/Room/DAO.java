package com.example.adabv2.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.adabv2.Model.Session;

import java.util.List;

@Dao
public interface DAO {
    @Insert
    void insertAllSession(Session... sessions);

    @Query("SELECT * FROM session")
    List<Session> getAllSessions();

    @Query("DELETE FROM session")
    void deleteAll();
}
