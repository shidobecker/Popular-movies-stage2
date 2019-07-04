package com.example.android.popular_movies_stage2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.popular_movies_stage2.R;
import com.example.android.popular_movies_stage2.databinding.ActivityDetailsBinding;
import com.example.android.popular_movies_stage2.model.domain.Movie;
import com.example.android.popular_movies_stage2.model.domain.Review;
import com.example.android.popular_movies_stage2.repository.MoviesDatabase;
import com.example.android.popular_movies_stage2.viewmodel.DetailsViewModel;
import com.example.android.popular_movies_stage2.viewmodel.DetailsViewModelFactory;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

public class DetailsActivity extends AppCompatActivity {

    private int movieId;

    private String apiKey;

    private String movieName;

    private ActivityDetailsBinding binding;

    private View movieInfoLayout;

    private DetailsViewModel detailsViewModel;

    private MoviesDatabase moviesDatabase;

    private ReviewAdapter reviewAdapter;

    public static final String MOVIE_ID_EXTRA = "MOVIE_ID_EXTRA";

    public static final String MOVIE_NAME_EXTRA = "MOVIE_NAME_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        //Just to show or hide info layout
        movieInfoLayout = findViewById(R.id.details_movie_information);

        if (getIntent().hasExtra(MOVIE_ID_EXTRA)) {
            movieId = getIntent().getIntExtra(MovieListActivity.MOVIE_ID_EXTRA, 0);
            apiKey = getString(R.string.API_KEY_TMDB);

            moviesDatabase = MoviesDatabase.getInstance(getApplicationContext());

            getMovieById();

            setupClicks();

            setupReviewAdapter();

        } else {
            Toast.makeText(this, getString(R.string.toast_message_intent_error), Toast.LENGTH_LONG).show();
            finish();
        }

    }


    private void setupClicks() {

        binding.detailsMovieInformation.detailsMovieFavoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookmarkMovie();
            }
        });

        binding.detailsMovieInformation.detailsMovieVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, VideosListActivity.class);
                intent.putExtra(MOVIE_ID_EXTRA, movieId);
                intent.putExtra(MOVIE_NAME_EXTRA, movieName);
                startActivity(intent);
            }
        });
    }


    private void setupReviewAdapter() {
        reviewAdapter = new ReviewAdapter();
        binding.detailsReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.detailsReviewsRecyclerView.setAdapter(reviewAdapter);

        detailsViewModel.getObservableReviews().observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(@Nullable List<Review> reviews) {
                if (reviews != null) {
                    handleReviewSuccess(reviews);
                } else {
                    handleReviewError();
                }
            }
        });


    }


    private void getMovieById() {
        setLoading();

        DetailsViewModelFactory detailsViewModelFactory = new DetailsViewModelFactory(moviesDatabase, movieId, apiKey);
        detailsViewModel = ViewModelProviders.of(this, detailsViewModelFactory).get(DetailsViewModel.class);

        detailsViewModel.getObservableMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie != null) {
                    handleMovieSuccess(movie);
                } else {
                    handleMovieError();
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


    private void handleMovieSuccess(Movie movie) {
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

        movieName = movie.getOriginalTitle();

        //After movie information has been displayed, fetch for movie reviews
        detailsViewModel.fetchMovieReviews(movieId, apiKey);

    }

    private void handleMovieError() {
        binding.detailsMovieOriginalTitle.setVisibility(View.GONE);
        binding.detailsErrorTitle.setVisibility(View.VISIBLE);
        binding.detailsErrorMessageBody.setVisibility(View.VISIBLE);
        binding.detailsProgressBar.setVisibility(View.GONE);
        movieInfoLayout.setVisibility(View.GONE);
    }


    private void handleReviewSuccess(List<Review> reviews) {
        binding.detailsReviewTitle.setVisibility(View.VISIBLE);
        binding.reviewsProgressBar.setVisibility(View.GONE);

        //In case there's no reviews for that movie, but no error occurred
        if (reviews.isEmpty()) {
            binding.detailsReviewsError.setVisibility(View.VISIBLE);
            binding.detailsReviewsError.setText(getString(R.string.review_no_review_found));
        } else {
            binding.detailsReviewsError.setVisibility(View.GONE);
        }

        reviewAdapter.setReviewList(reviews);

    }

    private void handleReviewError() {
        binding.detailsReviewTitle.setVisibility(View.VISIBLE);

        binding.reviewsProgressBar.setVisibility(View.GONE);

        binding.detailsReviewsError.setVisibility(View.VISIBLE);
        binding.detailsReviewsError.setText(getString(R.string.review_list_connection_message));

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

        binding.reviewsProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


}
