package com.example.android.popular_movies_stage2.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class VideoListViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final int movieId;

    private final String apiKey;


    public VideoListViewModelFactory(int movieId, String apiKey) {
        this.movieId = movieId;
        this.apiKey = apiKey;
    }


    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new VideoListViewModel(movieId, apiKey);
    }

}
