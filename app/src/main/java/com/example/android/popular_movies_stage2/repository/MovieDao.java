package com.example.android.popular_movies_stage2.repository;

import com.example.android.popular_movies_stage2.model.domain.Movie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<Movie> fetchMovieById(int id);

    @Query("SELECT * FROM movie")
    List<Movie> fetchMovies();

    @Insert
    void saveMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);


}
