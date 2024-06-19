package com.example.movieselector;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.movieselector.databinding.ActivityDetailsBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class detailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        MovieI movieApi = MovieApi.getRetrofitInstance().create(MovieI.class);
        Call<GetDetailsSchema> call = movieApi.getDetails(MovieApi.API_KEY, id);

        call.enqueue(new Callback<GetDetailsSchema>() {
            @Override
            public void onResponse(Call<GetDetailsSchema> call, Response<GetDetailsSchema> response) {
                if (response.isSuccessful()) {
                    GetDetailsSchema s = response.body();
                    Log.d("titulo: ",s.getTitle());
                    if(s == null) return;
                        TextView title_view = findViewById(R.id.title_text);
                        title_view.setText(s.getTitle());

                    ImageView poster = findViewById(R.id.poster);
                    Picasso.get()
                            .load(s.getPoster())
                            .into(poster);

                    TextView description_view = findViewById(R.id.descripcion);

                    String description = "Descripcion: " + s.getPlot();

                    description_view.setText(description);
                }
            }
            @Override
            public void onFailure(Call<GetDetailsSchema> call, Throwable t) {
                Log.d("error_1", t.getMessage());
                Toast.makeText(detailsActivity.this,"ERROR",Toast.LENGTH_SHORT);
            }
        });
    }
}