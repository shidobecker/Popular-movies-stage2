package com.example.android.popular_movies_stage2.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.popular_movies_stage2.R;
import com.example.android.popular_movies_stage2.databinding.ActivityDetailsBinding;
import com.example.android.popular_movies_stage2.model.domain.Movie;
import com.example.android.popular_movies_stage2.repository.MoviesDatabase;
import com.example.android.popular_movies_stage2.viewmodel.DetailsViewModel;
import com.example.android.popular_movies_stage2.viewmodel.DetailsViewModelFactory;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class DetailsActivity extends AppCompatActivity {

    private int movieId;

    private String apiKey;

    ActivityDetailsBinding binding;

    private View movieInfoLayout;

    private DetailsViewModel detailsViewModel;

    private MoviesDatabase moviesDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        //Just to show or hide info layout
        movieInfoLayout = findViewById(R.id.details_movie_information);

        if (getIntent().hasExtra(MovieListActivity.MOVIE_ID_EXTRA)) {
            movieId = getIntent().getIntExtra(MovieListActivity.MOVIE_ID_EXTRA, 0);
            apiKey = getString(R.string.API_KEY_TMDB);

            moviesDatabase = MoviesDatabase.getInstance(getApplicationContext());

            getMovieById();

            binding.detailsMovieInformation.detailsMovieFavoriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bookmarkMovie();
                }
            });

        } else {
            Toast.makeText(this, getString(R.string.toast_message_intent_error), Toast.LENGTH_LONG).show();
            finish();
        }

    }


    private void getMovieById() {
        setLoading();

        DetailsViewModelFactory detailsViewModelFactory = new DetailsViewModelFactory(moviesDatabase, movieId, apiKey);
        detailsViewModel = ViewModelProviders.of(this, detailsViewModelFactory).get(DetailsViewModel.class);

        detailsViewModel.getObservableMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie != null) {
                    handleSuccess(movie);
                } else {
                    handleError();
                }
            }
        });


        detailsViewModel.getIsBookmarked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isBookmarked) {
                if (isBookmarked != null) {
                    if (isBookmarked) {
                        binding.detailsMovieInformation.detailsMovieFavoriteImage.setImageResource(R.drawable.ic_start_fill);
                    } else {
                        binding.detailsMovieInformation.detailsMovieFavoriteImage.setImageResource(R.drawable.ic_star_outline);
                    }
                }
            }
        });
    }


    private void handleSuccess(Movie movie) {
        movieInfoLayout.setVisibility(View.VISIBLE);
        binding.detailsProgressBar.setVisibility(View.GONE);
        binding.detailsErrorTitle.setVisibility(View.GONE);
        binding.detailsErrorMessageBody.setVisibility(View.GONE);

        binding.detailsMovieOriginalTitle.setVisibility(View.VISIBLE);
        binding.detailsMovieOriginalTitle.setText(movie.getOriginalTitle());
        binding.detailsMovieInformation.detailsMovieDescription.setText(movie.getOverview());
        binding.detailsMovieInformation.detailsMovieReleaseYear.setText(getMovieYear(movie.getReleaseDate()));
        binding.detailsMovieInformation.detailsMovieRating.setText(String.format(Locale.getDefault(),
                movie.getUserRating().toString()));

        String posterPath = Movie.BASE_POSTER_PATH.concat(movie.getPosterPath());
        Picasso.get().load(posterPath)
                .error(R.drawable.ic_error_image)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(binding.detailsMovieInformation.detailsMovieImage);


    }

    private String getMovieYear(String releaseDate) {
        try {
            return releaseDate.substring(0, 4);
        } catch (IndexOutOfBoundsException i) {
            return releaseDate;
        }

    }


    private void bookmarkMovie() {
        Boolean bookmarked = detailsViewModel.getIsBookmarked().getValue();
        if (bookmarked != null) {
            if (bookmarked) {
                detailsViewModel.removeBookmarkedMovie();
            } else {
                detailsViewModel.addBookmarkMovie();
            }
        }
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
