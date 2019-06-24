package com.example.android.popular_movies_stage2.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popular_movies_stage2.R;
import com.example.android.popular_movies_stage2.api.MoviesApi;
import com.example.android.popular_movies_stage2.api.RetrofitClient;
import com.example.android.popular_movies_stage2.model.domain.Movie;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private int movieId;

    private String apiKey;

    private ProgressBar progressBar;

    private TextView errorMessage;

    private ScrollView contentView;

    private ImageView movieImage;

    private TextView originalTitle;

    private TextView releaseDate;

    private TextView overview;

    private TextView userRating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getIntent().hasExtra(MovieListActivity.MOVIE_ID_EXTRA)) {
            movieId = getIntent().getIntExtra(MovieListActivity.MOVIE_ID_EXTRA, 0);
            apiKey = getString(R.string.API_KEY_TMDB);

            setupViews();

            getMovieById();

        } else {
            Toast.makeText(this, getString(R.string.toast_message_intent_error), Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private void setupViews(){
        contentView = findViewById(R.id.details_movie_content);
        progressBar = findViewById(R.id.details_movie_progress_bar);
        errorMessage = findViewById(R.id.details_movie_error_message);

        movieImage = findViewById(R.id.details_movie_image);
        originalTitle = findViewById(R.id.details_movie_original_title);
        overview = findViewById(R.id.details_movie_overview);
        userRating = findViewById(R.id.details_movie_user_rating);
        releaseDate = findViewById(R.id.details_movie_release_date);
    }


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
        progressBar.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.GONE);

        originalTitle.setText(responseMovie.getOriginalTitle());
        overview.setText(responseMovie.getOverview());
        releaseDate.setText(responseMovie.getReleaseDate());
        userRating.setText(String.format(Locale.getDefault(), responseMovie.getUserRating().toString()));

        String posterPath = Movie.BASE_POSTER_PATH.concat(responseMovie.getPosterPath());
        Picasso.get().load(posterPath)
                .error(R.drawable.ic_error_image)
                .into(movieImage);
    }

    private void setLoading() {
        progressBar.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);

    }


    private void handleError() {
        errorMessage.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
    }


}
