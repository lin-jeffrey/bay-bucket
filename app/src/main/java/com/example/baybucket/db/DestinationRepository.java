package com.example.baybucket.db;

import android.content.Context;

import androidx.room.Room;

import com.example.baybucket.models.Destination;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DestinationRepository {
    private final DestinationDatabase destinationDatabase;
    public DestinationRepository(Context context){
        final String DB_NAME = "destination_db";
        destinationDatabase = Room.databaseBuilder(context, DestinationDatabase.class, DB_NAME).build();
    }


    public void insertDestination(String destName, String bucketListName) {
        Destination destination = new Destination(destName, bucketListName);
        insertDestination(destination);
    }

    ExecutorService executor = Executors.newSingleThreadExecutor();

    public void insertDestination(final Destination destination) {
        executor.execute(() -> destinationDatabase.destinationDao().insertDestination(destination));
    }

    public void updateDestination(final Destination destination) {
        executor.execute(() -> destinationDatabase.destinationDao().updateDestination(destination));
    }

    public void deleteDestination(final Destination destination) {
        executor.execute(() -> destinationDatabase.destinationDao().deleteDestination(destination));
    }

    public List<Destination> getAll() {
        ExecutorService executorService =
                new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>());

        List<Destination> list = null;

        Callable<List<Destination>> callableTask = () -> destinationDatabase.destinationDao().getAll();

        Future<List<Destination>> future = executorService.submit(callableTask);
        try{
            list = future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }

        executorService.shutdown();

        return list;
    }

    public List<Destination> getDestByName(String destinationName) {
        ExecutorService executorService =
                new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>());

        List<Destination> list = null;

        Callable<List<Destination>> callableTask = () -> destinationDatabase.destinationDao().getDestByName(destinationName);

        Future<List<Destination>> future = executorService.submit(callableTask);
        try{
            list = future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }

        executorService.shutdown();

        return list;
    }

    public List<Destination> getDestByBucketList(String bucketListName) {
        ExecutorService executorService =
                new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>());

        List<Destination> list = null;

        Callable<List<Destination>> callableTask = () -> destinationDatabase.destinationDao().getDestByBucketList(bucketListName);

        Future<List<Destination>> future = executorService.submit(callableTask);
        try{
            list = future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }

        executorService.shutdown();

        return list;
    }
}
