package com.example.movieselector;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieI {
    @GET("/")
    Call<SearchSchema> getMovies(@Query("apikey") String apiKey, @Query("s") String title);
}