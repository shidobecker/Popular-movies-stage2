package com.example.android.popular_movies_stage2.model.remote;

import com.example.android.popular_movies_stage2.model.domain.Video;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideosResult {

    @SerializedName("id")
    private int movieId;

    @SerializedName("results")
    private List<Video> videos;


    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
