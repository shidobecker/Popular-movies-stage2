package com.example.android.popular_movies_stage2.viewmodel;

import com.example.android.popular_movies_stage2.repository.MoviesDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MoviesDatabase database;

    private int movieId;

    private String apiKey;

    public DetailsViewModelFactory(MoviesDatabase database, int movieId, String apiKey) {
        this.database = database;
        this.movieId = movieId;
        this.apiKey = apiKey;
    }


    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailsViewModel(database, movieId, apiKey);
    }

}
