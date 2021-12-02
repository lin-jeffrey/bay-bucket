package com.example.baybucket.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.baybucket.models.Destination;

@Database(entities = {Destination.class},version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class DestinationDatabase extends RoomDatabase {
    public abstract DestinationDao destinationDao();
}
