package com.example.movieselector;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.flexbox.FlexboxLayout;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    int page = 1;
    String query = "";

    private RadioGroup radioGroup;
    private RadioButton series_radius, movie_radius, episodes_radius;

    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        radioGroup = findViewById(R.id.radioGroup);
        series_radius = findViewById(R.id.series_radius);
        movie_radius = findViewById(R.id.movies_radius);
        episodes_radius = findViewById(R.id.episodes_radius);
        this.type = "series";
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                String selectedType =selectedRadioButton.getTag().toString();
                type = selectedType;
            }
        });
    }

    public void searchSubmit(View view){
        EditText searchText = findViewById(R.id.searchText);

        String query = searchText.getText().toString();
        if(query.length() > 3){
            FlexboxLayout parentLayout = findViewById(R.id.flexboxLayout);
            parentLayout.removeAllViews();
            this.page = 1;
            this.query = query;
            this.search(query, 1);
        }
    }

    void search(String query,int page){
        MovieI movieApi = MovieApi.getRetrofitInstance().create(MovieI.class);

        Call<SearchSchema> call = movieApi.getMovies(MovieApi.API_KEY,query,page,this.type);
        call.enqueue(new Callback<SearchSchema>() {
            @Override
            public void onResponse(Call<SearchSchema> call, Response<SearchSchema> response) {
                if (response.isSuccessful()) {
                    SearchSchema s = response.body();
                    if(s == null) return;
                    List<Search> movies = s.Search;
                    if(movies == null) return;
                    createDynamicLayout(movies);
                    Button load = findViewById(R.id.load_data);
                    load.setVisibility(View.VISIBLE);
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

    public void nextPage(View view){
        this.page = this.page + 1;
        search(this.query,this.page);
    }
    private void createDynamicLayout(List<Search> data) {
        FlexboxLayout parentLayout = findViewById(R.id.flexboxLayout); // El LinearLayout padre en tu XML
        if(parentLayout == null || data == null) return;
        for (Search item : data) {

            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            linearLayout.setMinimumHeight(150);
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
            imageButton.setTag(item.imdbID);

            LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            imageLayoutParams.gravity = Gravity.CENTER;
            imageButton.setLayoutParams(imageLayoutParams);
            Picasso.get()
                    .load(item.Poster)
                    .into(imageButton, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            imageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String imdbId = (String) v.getTag();

                                    if(imdbId == null) return;

                                    openDetails(imdbId);
                                }
                            });
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });

            linearLayout.addView(textView);
            linearLayout.addView(imageButton);

            parentLayout.addView(linearLayout);
            imageButton.setOnClickListener(v -> {

            });


        }
    }

    void openDetails(String id){
        Intent intent = new Intent(MainActivity.this,detailsActivity.class);
        Log.d("id main activity: ",id);
        intent.putExtra("id",id);
        intent.putExtra("apiKey",MovieApi.API_KEY);
        startActivity(intent);
    }
}