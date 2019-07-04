package com.example.android.popular_movies_stage2.viewmodel;

import com.example.android.popular_movies_stage2.api.MoviesApi;
import com.example.android.popular_movies_stage2.api.RetrofitClient;
import com.example.android.popular_movies_stage2.model.domain.Video;
import com.example.android.popular_movies_stage2.model.remote.VideosResult;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoListViewModel extends ViewModel {

    private final MutableLiveData<List<Video>> observableVideos = new MutableLiveData<>();

    public VideoListViewModel(final int movieId, final String apiKey) {
        MoviesApi moviesApi = RetrofitClient.getInstance(apiKey).getRetrofit().create(MoviesApi.class);

        final Call<VideosResult> call = moviesApi.getMovieVideos(movieId);

        call.enqueue(new Callback<VideosResult>() {
            @Override
            public void onResponse(Call<VideosResult> call, Response<VideosResult> response) {
                if (response.isSuccessful()) {
                    observableVideos.postValue(response.body().getVideos());
                } else {
                    observableVideos.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<VideosResult> call, Throwable t) {
                observableVideos.postValue(null);

            }
        });
    }


    public MutableLiveData<List<Video>> getObservableVideos() {
        return observableVideos;
    }
}
