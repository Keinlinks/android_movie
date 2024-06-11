package com.example.movieselector;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        MovieI movieApi = MovieApi.getRetrofitInstance().create(MovieI.class);
        Call<SearchSchema> call = movieApi.getMovies(MovieApi.API_KEY,"batman");
        Toast.makeText(this,"Iniciando aplicacion",Toast.LENGTH_SHORT);
        call.enqueue(new Callback<SearchSchema>() {
            @Override
            public void onResponse(Call<SearchSchema> call, Response<SearchSchema> response) {
                if (response.isSuccessful()) {
                    SearchSchema s = response.body();
                    Log.d("prueba", s.totalResults);
                    Toast.makeText(MainActivity.this, "Exitoso: " + response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("error_2", "response vacio");
                    Toast.makeText(MainActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<SearchSchema> call, Throwable t) {
                Log.d("error_1", t.getMessage());
               Toast.makeText(MainActivity.this,"ERROR",Toast.LENGTH_SHORT);
            }
        });
    }
}