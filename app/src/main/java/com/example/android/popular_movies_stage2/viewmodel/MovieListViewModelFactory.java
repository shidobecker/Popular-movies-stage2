package com.example.android.popular_movies_stage2.viewmodel;

import com.example.android.popular_movies_stage2.repository.MoviesDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MovieListViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MoviesDatabase database;
    private String apiKey;


    public MovieListViewModelFactory(String apiKey, MoviesDatabase database) {
        this.apiKey = apiKey;
        this.database = database;

    }

    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MovieListViewModel(apiKey, database);
    }
}
