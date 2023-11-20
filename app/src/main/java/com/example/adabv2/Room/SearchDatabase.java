package com.example.adabv2.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.adabv2.Model.Search;

@Database(entities = {Search.class}, version = 1)
public abstract class SearchDatabase extends RoomDatabase {
    public abstract DAOClass searchDAO();
}
