package com.example.android.popular_movies_stage2.api;

import com.example.android.popular_movies_stage2.model.domain.Movie;
import com.example.android.popular_movies_stage2.model.remote.MoviesResult;
import com.example.android.popular_movies_stage2.model.remote.ReviewResult;
import com.example.android.popular_movies_stage2.model.remote.VideosResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MoviesApi {

    String MOVIES_BASE_PATH = "https://api.themoviedb.org/3/movie/";

    @GET("top_rated")
    Call<MoviesResult> getTopRatedMovies();

    @GET("popular")
    Call<MoviesResult> getPopularMovies();

    @GET("{id}")
    Call<Movie> getMovieById(@Path("id") int movieId);

    @GET("{id}/reviews")
    Call<ReviewResult> getMovieReviews(@Path("id") int movieId);

    @GET("{id}/videos")
    Call<VideosResult> getMovieVideos(@Path("id") int movieId);


}
