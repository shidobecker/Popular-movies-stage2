package com.example.android.popular_movies_stage2.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.popular_movies_stage2.R;
import com.example.android.popular_movies_stage2.databinding.ActivityMovieListBinding;
import com.example.android.popular_movies_stage2.model.domain.Movie;
import com.example.android.popular_movies_stage2.repository.MoviesDatabase;
import com.example.android.popular_movies_stage2.utils.MoviesPreferences;
import com.example.android.popular_movies_stage2.utils.SearchCriteria;
import com.example.android.popular_movies_stage2.viewmodel.MovieListViewModel;
import com.example.android.popular_movies_stage2.viewmodel.MovieListViewModelFactory;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

public class MovieListActivity extends AppCompatActivity implements MovieAdapter.MovieClickListener {

    private MovieAdapter movieAdapter;

    private String apiKey;

    static final String MOVIE_ID_EXTRA = "MOVIE_ID_EXTRA";

    private MovieListViewModel viewModel;

    private ActivityMovieListBinding binding;

    private MoviesDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_list);

        apiKey = getString(R.string.API_KEY_TMDB);

        database = MoviesDatabase.getInstance(getApplicationContext());

        setupViewModel();

        setupRecyclerView();

        observeMovies();

    }

    private void setupViewModel() {
        MovieListViewModelFactory viewModelFactory = new MovieListViewModelFactory(apiKey, database);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieListViewModel.class);

        String searchCriteria = MoviesPreferences.getLastSearchCriteria(this);

        if (searchCriteria.equals(SearchCriteria.FAVORITES.name())) {
            getMoviesByFavorites();
        } else if (searchCriteria.equals(SearchCriteria.POPULAR.name())) {
            getMoviesByPopularity();
        } else {
            getMoviesByTopRated();
        }
    }

    private void observeMovies() {
        viewModel.getObservableMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if (movies != null) {
                    handleSuccess();
                    movieAdapter.setMovies(movies);
                } else {
                    handleError();
                    movieAdapter.setMovies(null);
                }
            }
        });

    }

    private void getMoviesByPopularity() {
        MoviesPreferences.setLastSearchCriteria(this, SearchCriteria.POPULAR);

        setLoading();

        viewModel.getMoviesByPopularity();
    }

    private void getMoviesByTopRated() {
        MoviesPreferences.setLastSearchCriteria(this, SearchCriteria.TOP_RATED);

        setLoading();

        viewModel.getMoviesByTopRated();
    }

    private void getMoviesByFavorites() {
        MoviesPreferences.setLastSearchCriteria(this, SearchCriteria.FAVORITES);

        setLoading();

        viewModel.getMoviesByFavorites();

        viewModel.getHasFavoriteMovies().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean hasFavorites) {
                if (hasFavorites != null && !hasFavorites) {
                    Toast.makeText(MovieListActivity.this, getString(R.string.no_favorites), Toast.LENGTH_SHORT).show();
                    handleSuccess();
                }
            }
        });
    }



    private void handleSuccess() {
        binding.moviesProgressBar.setVisibility(View.GONE);
        binding.moviesRecyclerView.setVisibility(View.VISIBLE);
        binding.moviesErrorMessage.setVisibility(View.GONE);
    }

    private void setLoading() {
        binding.moviesProgressBar.setVisibility(View.VISIBLE);
        binding.moviesRecyclerView.setVisibility(View.GONE);
        binding.moviesErrorMessage.setVisibility(View.GONE);
    }


    private void handleError() {
        binding.moviesErrorMessage.setVisibility(View.VISIBLE);
        binding.moviesProgressBar.setVisibility(View.GONE);
        binding.moviesRecyclerView.setVisibility(View.GONE);
    }


    private void setupRecyclerView() {
        movieAdapter = new MovieAdapter(this);

        binding.moviesRecyclerView.setLayoutManager(new GridLayoutManager(this,
                calculateNoOfColumns(),
                GridLayoutManager.VERTICAL, false));

        binding.moviesRecyclerView.setAdapter(movieAdapter);
    }

    private int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }


    @Override
    public void onClickMovie(int id) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.MOVIE_ID_EXTRA, id);
        startActivity(intent);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder menuBuilder = (MenuBuilder) menu;
            menuBuilder.setOptionalIconsVisible(true);
        }
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

        if (itemId == R.id.action_favorites) {
            getMoviesByFavorites();
        }

        return super.onOptionsItemSelected(item);

    }


}
