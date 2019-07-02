package com.example.android.popular_movies_stage2.repository;

import android.content.Context;
import android.util.Log;

import com.example.android.popular_movies_stage2.model.domain.Movie;
import com.example.android.popular_movies_stage2.model.domain.Review;
import com.example.android.popular_movies_stage2.model.domain.Video;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class, Review.class, Video.class}, version = 1, exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase {

    private static final String LOG_TAG = MoviesDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "popularmovies";
    private static MoviesDatabase dbInstance;

    public static MoviesDatabase getInstance(Context context) {
        if (dbInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MoviesDatabase.class, MoviesDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return dbInstance;
    }

    public abstract MovieDao getMovieDao();

    public abstract VideoDao getVideoDao();

    public abstract ReviewDao getReviewDao();

}
