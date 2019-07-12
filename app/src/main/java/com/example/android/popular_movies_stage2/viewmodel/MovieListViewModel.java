package com.example.android.popular_movies_stage2.viewmodel;

import com.example.android.popular_movies_stage2.api.MoviesApi;
import com.example.android.popular_movies_stage2.api.RetrofitClient;
import com.example.android.popular_movies_stage2.model.domain.Movie;
import com.example.android.popular_movies_stage2.model.remote.MoviesResult;
import com.example.android.popular_movies_stage2.repository.AppExecutors;
import com.example.android.popular_movies_stage2.repository.MoviesDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListViewModel extends ViewModel {

    private final MediatorLiveData<List<Movie>> observableMovies = new MediatorLiveData<>();

    private MutableLiveData<Boolean> hasFavoriteMovies = new MutableLiveData<>();

    private String apiKey;

    private MoviesDatabase database;

    MovieListViewModel(String apiKey, MoviesDatabase database) {
        this.apiKey = apiKey;
        this.database = database;
    }

    public void getMoviesByPopularity() {

        MoviesApi moviesApi = RetrofitClient.getInstance(apiKey).getRetrofit().create(MoviesApi.class);
        Call<MoviesResult> popularMovies = moviesApi.getPopularMovies();

        popularMovies.enqueue(new Callback<MoviesResult>() {
            @Override
            public void onResponse(Call<MoviesResult> call, Response<MoviesResult> response) {
                if (response.isSuccessful()) {
                    observableMovies.setValue(response.body().getMovies());
                } else {
                    observableMovies.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MoviesResult> call, Throwable t) {
                observableMovies.setValue(null);
            }
        });
    }


    public void getMoviesByTopRated() {

        MoviesApi moviesApi = RetrofitClient.getInstance(apiKey).getRetrofit().create(MoviesApi.class);
        Call<MoviesResult> popularMovies = moviesApi.getTopRatedMovies();

        popularMovies.enqueue(new Callback<MoviesResult>() {
            @Override
            public void onResponse(Call<MoviesResult> call, Response<MoviesResult> response) {
                if (response.isSuccessful()) {
                    observableMovies.setValue(response.body().getMovies());
                } else {
                    observableMovies.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MoviesResult> call, Throwable t) {
                observableMovies.setValue(null);
            }
        });
    }

    public void getMoviesByFavorites() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                LiveData<List<Movie>> favoritesByRoom = database.getMovieDao().fetchMovies();

                observableMovies.addSource(favoritesByRoom, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        if (movies != null && !movies.isEmpty()) {
                            //No need to update movies if there's no favorite movie saved
                            hasFavoriteMovies.postValue(true);
                            observableMovies.postValue(movies);
                        } else {
                            hasFavoriteMovies.postValue(false);
                        }
                    }
                });



            }
        });

    }

    public MutableLiveData<List<Movie>> getObservableMovies() {
        return observableMovies;
    }

    public MutableLiveData<Boolean> getHasFavoriteMovies() {
        return hasFavoriteMovies;
    }
}
