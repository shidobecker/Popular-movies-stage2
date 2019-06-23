package com.example.android.popular_movies_stage2.model.remote;

import com.example.android.popular_movies_stage2.model.domain.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResult {

    @SerializedName("page")
    private String page;

    @SerializedName("results")
    private List<Movie> movies;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
