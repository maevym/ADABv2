package com.example.adabv2.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.adabv2.Model.Session;

@Database(entities = {Session.class}, version = 1)
public abstract class SessionDatabase extends RoomDatabase {
    public abstract DAO sessionDAO();
}
