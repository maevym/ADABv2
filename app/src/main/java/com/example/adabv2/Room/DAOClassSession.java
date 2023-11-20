package com.example.adabv2.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.adabv2.Model.ClassSession;
import com.example.adabv2.Model.Search;

import java.util.List;

@Dao
public interface DAOClassSession {
    @Insert
    void insertClassSession(ClassSession... classSessions);

    @Query("SELECT * FROM classSession")
    List<ClassSession> getAllClassSession();


    @Query("DELETE FROM classSession")
    void deleteAllClassSession();
}
