package com.example.baybucket.db;

import android.content.Context;

import androidx.room.Room;

import com.example.baybucket.models.Memory;
import com.example.baybucket.ui.profile.ProfileFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MemoryRepository {
    private final MemoryDatabase memoryDatabase;
    public MemoryRepository(Context context){
        final String DB_NAME = "memory_db";
        memoryDatabase = Room.databaseBuilder(context, MemoryDatabase.class, DB_NAME).build();
    }

    public void insertMemory(String email, String destinationName, String coordinates, Date checkInDate) {
        Memory memory = new Memory(email, destinationName, coordinates, checkInDate);
        insertMemory(memory);
    }

    public void insertMemory(String email, String destinationName, String coordinates, Date checkInDate, String caption, String imageUri) {
        Memory memory = new Memory(email, destinationName, coordinates, checkInDate, caption, imageUri);
        insertMemory(memory);
    }

    ExecutorService executor = Executors.newSingleThreadExecutor();

    public void insertMemory(final Memory memory) {
        executor.execute(() -> memoryDatabase.memoryDao().insertMemory(memory));
    }

    public void updateMemory(final Memory memory) {
        executor.execute(() -> memoryDatabase.memoryDao().updateMemory(memory));
    }

    public void deleteMemory(final Memory memory) {
        executor.execute(() -> memoryDatabase.memoryDao().deleteMemory(memory));
    }

    public List<Memory> getAll() {
        ExecutorService executorService =
                new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>());

        List<Memory> list = null;

        Callable<List<Memory>> callableTask = () -> memoryDatabase.memoryDao().getAll();

        Future<List<Memory>> future = executorService.submit(callableTask);
        try{
            list = future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }

        executorService.shutdown();

        return list;
    }

    public List<Memory> getMemoriesByUser(String email) {
        ExecutorService executorService =
                new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>());

        List<Memory> list = null;

        Callable<List<Memory>> callableTask = () -> memoryDatabase.memoryDao().getMemoriesByUser(email);

        Future<List<Memory>> future = executorService.submit(callableTask);
        try{
            list = future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }

        executorService.shutdown();

        return list;
    }

    public List<Memory> getMemoryByUserAndDest(String email, String destinationName) {
        ExecutorService executorService =
                new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>());

        List<Memory> list = null;

        Callable<List<Memory>> callableTask = () -> memoryDatabase.memoryDao().getMemoryByUserAndDest(email, destinationName);

        Future<List<Memory>> future = executorService.submit(callableTask);
        try{
            list = future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }

        executorService.shutdown();

        return list;
    }
}
