package com.example.android.popular_movies_stage2.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popular_movies_stage2.R;
import com.example.android.popular_movies_stage2.api.MoviesApi;
import com.example.android.popular_movies_stage2.api.RetrofitClient;
import com.example.android.popular_movies_stage2.model.domain.Movie;
import com.example.android.popular_movies_stage2.model.remote.MoviesResult;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity implements MovieAdapter.MovieClickListener {

    private RecyclerView moviesRecyclerView;

    private MovieAdapter movieAdapter;

    private ProgressBar progressBar;

    private TextView errorMessage;

    private String apiKey;

    static final String MOVIE_ID_EXTRA = "MOVIE_ID_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRecyclerView = findViewById(R.id.movies_recycler_view);
        progressBar = findViewById(R.id.movies_progress_bar);
        errorMessage = findViewById(R.id.movies_error_message);

        apiKey = getString(R.string.API_KEY_TMDB);

        setupRecyclerView();

        getMoviesByPopularity();

    }

    private void getMoviesByPopularity() {
        setLoading();

        MoviesApi moviesApi = RetrofitClient.getInstance(apiKey).getRetrofit().create(MoviesApi.class);
        Call<MoviesResult> popularMovies = moviesApi.getPopularMovies();

        popularMovies.enqueue(new Callback<MoviesResult>() {
            @Override
            public void onResponse(Call<MoviesResult> call, Response<MoviesResult> response) {
                if (response.isSuccessful()) {
                    handleSuccess(response);
                } else {
                    handleError();
                }
            }

            @Override
            public void onFailure(Call<MoviesResult> call, Throwable t) {
                handleError();

            }
        });
    }

    private void getMoviesByTopRated() {
        setLoading();

        MoviesApi moviesApi = RetrofitClient.getInstance(apiKey).getRetrofit().create(MoviesApi.class);
        Call<MoviesResult> popularMovies = moviesApi.getTopRatedMovies();

        popularMovies.enqueue(new Callback<MoviesResult>() {
            @Override
            public void onResponse(Call<MoviesResult> call, Response<MoviesResult> response) {
                if (response.isSuccessful()) {
                    handleSuccess(response);
                } else {
                    handleError();
                }
            }

            @Override
            public void onFailure(Call<MoviesResult> call, Throwable t) {
                handleError();
            }
        });
    }


    private void handleSuccess(Response<MoviesResult> response) {
        final ArrayList<Movie> movies;

        MoviesResult moviesResult = response.body();
        movies = new ArrayList<>(moviesResult.getMovies());

        progressBar.setVisibility(View.GONE);
        moviesRecyclerView.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.GONE);
        movieAdapter.setMovies(movies);
    }

    private void setLoading() {
        progressBar.setVisibility(View.VISIBLE);
        moviesRecyclerView.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);

    }


    private void handleError() {
        errorMessage.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        moviesRecyclerView.setVisibility(View.GONE);
        movieAdapter.setMovies(null);
    }


    private void setupRecyclerView() {
        movieAdapter = new MovieAdapter(this);

        int orientation = getResources().getConfiguration().orientation;

        int spanCount = 2;

        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            spanCount = 3;
        }

        moviesRecyclerView.setLayoutManager(new GridLayoutManager(this,
                spanCount,
                GridLayoutManager.VERTICAL, false));

        moviesRecyclerView.setAdapter(movieAdapter);
    }

    @Override
    public void onClickMovie(int id) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(MOVIE_ID_EXTRA, id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_popular) {
            getMoviesByPopularity();
        }
        if (itemId == R.id.action_topRated) {
            getMoviesByTopRated();
        }

        return super.onOptionsItemSelected(item);

    }


}
