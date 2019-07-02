package com.example.android.popular_movies_stage2.repository;

import com.example.android.popular_movies_stage2.model.domain.Movie;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<Movie> fetchMovieById(int id);

}
