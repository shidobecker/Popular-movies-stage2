package com.example.android.popular_movies_stage2.model.domain;

import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Video {

    @SerializedName("id")
    @PrimaryKey
    private String id;

    private int movieId;

    @SerializedName("iso_639_1")
    private String ISO639_1;

    @SerializedName("iso_3166_1")
    private String ISO3166_1;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    @SerializedName("site")
    private String site;

    @SerializedName("size")
    private int size;

    @SerializedName("type")
    private String type;


    public Video(String id, int movieId, String ISO639_1, String ISO3166_1, String key, String name, String site, int size, String type) {
        this.id = id;
        this.movieId = movieId;
        this.ISO639_1 = ISO639_1;
        this.ISO3166_1 = ISO3166_1;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getISO639_1() {
        return ISO639_1;
    }

    public void setISO639_1(String ISO639_1) {
        this.ISO639_1 = ISO639_1;
    }

    public String getISO3166_1() {
        return ISO3166_1;
    }

    public void setISO3166_1(String ISO3166_1) {
        this.ISO3166_1 = ISO3166_1;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
