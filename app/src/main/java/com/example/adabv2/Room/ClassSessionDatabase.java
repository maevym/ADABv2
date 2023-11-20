package com.example.adabv2.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.adabv2.Model.ClassSession;
import com.example.adabv2.Model.Search;

@Database(entities = {ClassSession.class}, version = 1)
public abstract class ClassSessionDatabase extends RoomDatabase {
    public abstract DAOClassSession classSessionDAO();

}


