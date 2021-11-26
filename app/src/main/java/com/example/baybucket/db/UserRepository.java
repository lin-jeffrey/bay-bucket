package com.example.baybucket.db;

import android.content.Context;

import androidx.room.Room;

import com.example.baybucket.models.User;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UserRepository {
    private final UserDatabase userDatabase;
    public UserRepository(Context context){
        final String DB_NAME = "user_db";
        userDatabase = Room.databaseBuilder(context, UserDatabase.class, DB_NAME).build();
    }

    public void insertUser(String username, String email, String password) {
        User user = new User(username, email, password);
        insertUser(user);
    }

    public void insertUser(String username, String email, String password, int points, String imageUri) {
        User user = new User(username, email, password, points, imageUri);
        insertUser(user);
    }

    ExecutorService executor = Executors.newSingleThreadExecutor();

    public void insertUser(final User user) {
        executor.execute(() -> userDatabase.userDao().insertUser(user));
    }

    public void updateUser(final User user) {
        executor.execute(() -> userDatabase.userDao().updateUser(user));
    }

    public void deleteUser(final User user) {
        executor.execute(() -> userDatabase.userDao().deleteUser(user));
    }

    public List<User> getAll() {
        ExecutorService executorService =
                new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>());

        List<User> list = null;

        Callable<List<User>> callableTask = () -> userDatabase.userDao().getAll();

        Future<List<User>> future = executorService.submit(callableTask);
        try{
            list = future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }

        executorService.shutdown();

        return list;
    }

    public List<User> getMemoriesByUser(String username) {
        ExecutorService executorService =
                new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>());

        List<User> list = null;

        Callable<List<User>> callableTask = () -> userDatabase.userDao().getUserByUsername(username);

        Future<List<User>> future = executorService.submit(callableTask);
        try{
            list = future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }

        executorService.shutdown();

        return list;
    }

    public List<User> getUserByEmail(String email) {
        ExecutorService executorService =
                new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>());

        List<User> list = null;

        Callable<List<User>> callableTask = () -> userDatabase.userDao().getUserByEmail(email);

        Future<List<User>> future = executorService.submit(callableTask);
        try{
            list = future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }

        executorService.shutdown();

        return list;
    }
}
