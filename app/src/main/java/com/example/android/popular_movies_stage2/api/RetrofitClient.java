package com.example.android.popular_movies_stage2.api;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance = null;
    private Retrofit retrofit;

    private RetrofitClient(final String apiKey){

        OkHttpClient client = buildClient(apiKey);

        retrofit = new Retrofit.Builder().baseUrl(MoviesApi.MOVIES_BASE_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    private OkHttpClient buildClient(final String apiKey)  {

        OkHttpClient.Builder builder =
                new OkHttpClient.Builder();

        //Adding API Key for every request
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", apiKey)
                        .build();

                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });


        return builder.build();
    }


    public static RetrofitClient getInstance(final String apiKey) {
        if (instance == null) {
            instance = new RetrofitClient(apiKey);
        }

        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }


}
