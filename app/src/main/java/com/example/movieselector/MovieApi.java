package com.example.movieselector;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieApi extends Service {
    public static final String API_KEY = "7d251fb9";
    private static final String BASE_URL = "https://www.omdbapi.com/";
    private static Retrofit retrofit;

    public MovieApi() {
    }
    public static Retrofit getRetrofitInstance() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}