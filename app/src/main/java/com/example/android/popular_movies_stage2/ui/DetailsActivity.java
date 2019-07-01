package com.example.android.popular_movies_stage2.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.android.popular_movies_stage2.R;
import com.example.android.popular_movies_stage2.api.MoviesApi;
import com.example.android.popular_movies_stage2.api.RetrofitClient;
import com.example.android.popular_movies_stage2.databinding.ActivityDetailsBinding;
import com.example.android.popular_movies_stage2.model.domain.Movie;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private int movieId;

    private String apiKey;

    ActivityDetailsBinding binding;

    private View movieInfoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        //Just to show or hide info layout
        movieInfoLayout = findViewById(R.id.details_movie_information);

        if (getIntent().hasExtra(MovieListActivity.MOVIE_ID_EXTRA)) {
            movieId = getIntent().getIntExtra(MovieListActivity.MOVIE_ID_EXTRA, 0);
            apiKey = getString(R.string.API_KEY_TMDB);

            getMovieById();

        } else {
            Toast.makeText(this, getString(R.string.toast_message_intent_error), Toast.LENGTH_LONG).show();
            finish();
        }

    }

    //TODO: SEARCH FOR THAT MOVIE ID ON ROOM DATABASE, IF THERE ISN'T , FETCH FROM API


    private void getMovieById() {
        setLoading();

        MoviesApi moviesApi = RetrofitClient.getInstance(apiKey).getRetrofit().create(MoviesApi.class);
        Call<Movie> movieById = moviesApi.getMovieById(movieId);

        movieById.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    handleSuccess(response.body());
                } else {
                    handleError();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                handleError();
            }
        });


    }


    private void handleSuccess(Movie responseMovie) {

        movieInfoLayout.setVisibility(View.VISIBLE);
        binding.detailsProgressBar.setVisibility(View.GONE);
        binding.detailsErrorTitle.setVisibility(View.GONE);
        binding.detailsErrorMessageBody.setVisibility(View.GONE);

        binding.detailsMovieOriginalTitle.setVisibility(View.VISIBLE);
        binding.detailsMovieOriginalTitle.setText(responseMovie.getOriginalTitle());
        binding.detailsMovieInformation.detailsMovieDescription.setText(responseMovie.getOverview());
        binding.detailsMovieInformation.detailsMovieReleaseYear.setText(responseMovie.getReleaseDate());
        binding.detailsMovieInformation.detailsMovieRating.setText(String.format(Locale.getDefault(), responseMovie.getUserRating().toString()));

        String posterPath = Movie.BASE_POSTER_PATH.concat(responseMovie.getPosterPath());
        Picasso.get().load(posterPath)
                .error(R.drawable.ic_error_image)
                .into(binding.detailsMovieInformation.detailsMovieImage);
    }

    private void setLoading() {
        binding.detailsProgressBar.setVisibility(View.VISIBLE);
        movieInfoLayout.setVisibility(View.GONE);
        binding.detailsErrorTitle.setVisibility(View.GONE);
        binding.detailsErrorMessageBody.setVisibility(View.GONE);
        binding.detailsMovieOriginalTitle.setVisibility(View.GONE);

    }


    private void handleError() {
        binding.detailsMovieOriginalTitle.setVisibility(View.GONE);
        binding.detailsErrorTitle.setVisibility(View.VISIBLE);
        binding.detailsErrorMessageBody.setVisibility(View.VISIBLE);
        binding.detailsProgressBar.setVisibility(View.GONE);
        movieInfoLayout.setVisibility(View.GONE);
    }


}
