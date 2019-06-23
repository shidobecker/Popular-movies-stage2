package com.example.android.popular_movies_stage2.model.remote;

import com.example.android.popular_movies_stage2.model.domain.Review;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewResult {

    @SerializedName("id")
    private int movieId;

    @SerializedName("results")
    private List<Review> reviews;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
