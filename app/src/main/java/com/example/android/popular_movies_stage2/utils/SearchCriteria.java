package com.example.android.popular_movies_stage2.utils;

public enum SearchCriteria {

    TOP_RATED("TOP_RATED"),
    POPULAR("POPULAR"),
    FAVORITES("FAVORITES");

    final String name;

    SearchCriteria(String name) {
        this.name = name;
    }


}
