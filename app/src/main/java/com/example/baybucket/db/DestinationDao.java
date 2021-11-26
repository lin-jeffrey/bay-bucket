package com.example.baybucket.db;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.baybucket.models.Destination;

import java.util.List;

@Dao
public interface DestinationDao {
    @Insert(onConflict = REPLACE)
    void insertDestination(Destination destination);

    @Update
    void updateDestination(Destination destination);

    @Delete
    void deleteDestination(Destination destination);

    @Query("SELECT * FROM Destination")
    List<Destination> getAll();

    @Query("SELECT * FROM Destination WHERE name =:destinationName")
    List<Destination> getDestByName(String destinationName);

    @Query("SELECT * FROM Destination WHERE bucketListName =:bucketListName")
    List<Destination> getDestByBucketList(String bucketListName);
}
