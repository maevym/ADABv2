package com.example.adabv2.Room;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.adabv2.Model.Search;

import java.util.List;

@Dao
public interface DAOClass {

    @Insert
    void insertSearchClass(Search... searches);

    @Query("SELECT * FROM search")
    List<Search> getAllSearch();


    @Query("DELETE FROM search")
    void deleteAllSearch();

}
