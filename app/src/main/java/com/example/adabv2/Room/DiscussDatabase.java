package com.example.adabv2.Room;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.adabv2.Model.Discuss;

@Database(entities = {Discuss.class}, version = 2, exportSchema = false)
public abstract class DiscussDatabase extends RoomDatabase {
    public abstract DAODiscuss discussWithMember();

}
