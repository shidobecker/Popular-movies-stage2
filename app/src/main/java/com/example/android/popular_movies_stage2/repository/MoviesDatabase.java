package com.example.android.popular_movies_stage2.repository;

import android.content.Context;

import com.example.android.popular_movies_stage2.model.domain.Movie;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "popularmovies";
    private static MoviesDatabase dbInstance;

    public static MoviesDatabase getInstance(Context context) {
        if (dbInstance == null) {
            synchronized (LOCK) {
                dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MoviesDatabase.class, MoviesDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return dbInstance;
    }

    public abstract MovieDao getMovieDao();


}
