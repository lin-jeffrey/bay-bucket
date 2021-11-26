package com.example.baybucket.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.baybucket.models.User;

@Database(entities = {User.class},version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class UserDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
