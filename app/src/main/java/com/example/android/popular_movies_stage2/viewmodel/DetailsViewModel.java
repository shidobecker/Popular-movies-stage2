package com.example.android.popular_movies_stage2.viewmodel;


import com.example.android.popular_movies_stage2.api.MoviesApi;
import com.example.android.popular_movies_stage2.api.RetrofitClient;
import com.example.android.popular_movies_stage2.model.domain.Movie;
import com.example.android.popular_movies_stage2.repository.AppExecutors;
import com.example.android.popular_movies_stage2.repository.MoviesDatabase;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsViewModel extends ViewModel {

    private LiveData<Movie> movieByRoom;

    private MutableLiveData<Movie> movieByApi = new MutableLiveData<>();

    private MediatorLiveData<Movie> observableMovie = new MediatorLiveData<>();

    private MutableLiveData<Boolean> isBookmarked = new MutableLiveData<>();

    private MoviesDatabase database;

    public DetailsViewModel(MoviesDatabase database, final int movieId, final String apiKey) {

        this.database = database;

        movieByRoom = this.database.getMovieDao().fetchMovieById(movieId);

        observableMovie.addSource(movieByRoom, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie != null) {
                    observableMovie.postValue(movie);
                    observableMovie.removeSource(movieByApi);

                    isBookmarked.postValue(true);

                } else {

                    fetchMovieByAPI(movieId, apiKey);

                    observableMovie.addSource(movieByApi, new Observer<Movie>() {
                        @Override
                        public void onChanged(@Nullable Movie movie) {
                            observableMovie.postValue(movie);

                            isBookmarked.postValue(false);
                        }
                    });

                }

            }
        });

    }


    public void fetchMovieByAPI(int movieId, String apiKey) {
        MoviesApi moviesApi = RetrofitClient.getInstance(apiKey).getRetrofit().create(MoviesApi.class);
        final Call<Movie> movieById = moviesApi.getMovieById(movieId);

        movieById.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    movieByApi.postValue(response.body());
                } else {
                    movieByApi.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                movieByApi.setValue(null);
            }
        });

    }

    public void addBookmarkMovie(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getMovieDao().saveMovie(observableMovie.getValue());
                getIsBookmarked().postValue(false);
            }
        });

    }

    public void removeBookmarkedMovie(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getMovieDao().deleteMovie(observableMovie.getValue());
                getIsBookmarked().postValue(true);
            }
        });
    }



    public MediatorLiveData<Movie> getObservableMovie() {
        return observableMovie;
    }
    public MutableLiveData<Boolean> getIsBookmarked() {
        return isBookmarked;
    }

}
