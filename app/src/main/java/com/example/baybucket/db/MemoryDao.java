package com.example.baybucket.db;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.baybucket.models.Memory;

import java.util.List;

@Dao
public interface MemoryDao {
    @Insert(onConflict = REPLACE)
    void insertMemory(Memory memory);

    @Update
    void updateMemory(Memory memory);

    @Delete
    void deleteMemory(Memory memory);

    @Query("SELECT * FROM Memory")
    List<Memory> getAll();

    @Query("SELECT * FROM Memory WHERE email =:email")
    List<Memory> getMemoriesByUser(String email);

    @Query("SELECT * FROM Memory WHERE email =:email AND destinationName =:destinationName")
    List<Memory> getMemoryByUserAndDest(String email, String destinationName);
}