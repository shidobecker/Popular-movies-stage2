package com.example.android.popular_movies_stage2.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.popular_movies_stage2.R;

public class MoviesPreferences {

    private static final String PREFS_KEY = "POPULAR_MOVIES_PREFS";

    private static final String CRITERIA_KEY = "CRITERIA_KEY";

    private static SharedPreferences sharedPrefs;

    private static SharedPreferences getPrefs(Context context) {

        if (sharedPrefs == null) {
            sharedPrefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        }
        return sharedPrefs;
    }

    public static void setLastSearchCriteria(Context context, SearchCriteria criteria) {
        getPrefs(context).edit().putString(CRITERIA_KEY, criteria.name).apply();
    }

    public static String getLastSearchCriteria(Context context) {
        String defaultCriteria = context.getString(R.string.criteria_default);
        return getPrefs(context).getString(CRITERIA_KEY, defaultCriteria);
    }

}
