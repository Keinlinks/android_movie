package com.example.movieselector;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        Toast.makeText(this,"Iniciando aplicacion",Toast.LENGTH_SHORT).show();

    }

    public void searchSubmit(View view){
        EditText searchText = findViewById(R.id.searchText);

        String query = searchText.getText().toString();

        MovieI movieApi = MovieApi.getRetrofitInstance().create(MovieI.class);

        Call<SearchSchema> call = movieApi.getMovies(MovieApi.API_KEY,query);
        call.enqueue(new Callback<SearchSchema>() {
            @Override
            public void onResponse(Call<SearchSchema> call, Response<SearchSchema> response) {
                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String jsonResponse = gson.toJson(response.body());
                    Log.d("respuesta: ", jsonResponse);

                    SearchSchema s = response.body();
                    List<Search> movies = s.Search;
                    createDynamicLayout(movies);
                    if(movies ==null) return;
                    for(Search movie: movies){
                        if(movie != null && movie.Title != null){
                            Log.d("movie: ", movie.Title);
                        }
                    }
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
    private void createDynamicLayout(List<Search> data) {
        LinearLayout parentLayout = findViewById(R.id.flexboxLayout); // El LinearLayout padre en tu XML
        if(parentLayout == null) return;
        for (Search item : data) {

            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(15, 0, 15, 0);
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            TextView textView = new TextView(this);
            LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            textView.setLayoutParams(textLayoutParams);
            textView.setGravity(Gravity.CENTER);
            textView.setText(item.Title);

            ImageButton imageButton = new ImageButton(this);
            LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            imageLayoutParams.gravity = Gravity.CENTER;
            imageButton.setLayoutParams(imageLayoutParams);


            linearLayout.addView(textView);
            linearLayout.addView(imageButton);

            parentLayout.addView(linearLayout);
        }
    }
}