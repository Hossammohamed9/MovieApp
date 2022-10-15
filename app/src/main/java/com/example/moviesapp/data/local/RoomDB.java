package com.example.moviesapp.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.moviesapp.data.entities.Favourites;
import com.example.moviesapp.data.entities.Movie;

@Database(entities = {Movie.class, Favourites.class}, version = 2, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {

    private static RoomDB roomDB;

    private static String DATABASE_NAME = "database";

    public synchronized static RoomDB getInstance(Context context){
        if (roomDB == null){
            roomDB = Room.databaseBuilder(context.getApplicationContext(),RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return roomDB;
    }

    public abstract UserDao userDao();
}
