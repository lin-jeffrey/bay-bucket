package com.example.baybucket.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.baybucket.models.Memory;

@Database(entities = {Memory.class},version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MemoryDatabase extends RoomDatabase {
    public abstract MemoryDao memoryDao();
}
