package com.example.flixster;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetails extends AppCompatActivity {

    public static final String VIDEO_FST = "https://api.themoviedb.org/3/movie/";
    public static final String VIDEO_SND = "/videos?api_key=44078a8234c8775235f52ddecaf3967d";
    private ActivityMovieDetailsBinding binding;
    public static final String TAG = "MovieDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        Intent i = getIntent();
        String title = i.getStringExtra("title");
        String overview = i.getStringExtra("overview");

        // Customize action bar (title and color)
        ActionBar toolbar;
        toolbar = getSupportActionBar();
        toolbar.setTitle(i.getStringExtra("title"));

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#7a2121"));
        toolbar.setBackgroundDrawable(colorDrawable);

        float voteAverage = (float) i.getDoubleExtra("movieRating", 0);

        binding.tvTitleDesc.setText(title);
        binding.tvOverviewDesc.setText(overview);
        binding.rbMovieRating.setRating(voteAverage / 2.0f);

        int radius = 30;
        int margin = 0;
        Glide.with(this)
                .load(i.getStringExtra("poster"))
                .transform(new RoundedCornersTransformation(radius, margin))
                .into(binding.ivPosterDesc);

        binding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = i.getIntExtra("id", 0);
                AsyncHttpClient client = new AsyncHttpClient();
                client.get(VIDEO_FST + id + VIDEO_SND, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Headers headers, JSON json) {
                        Log.d(TAG, "onSuccess");
                        JSONObject jsonObject = json.jsonObject;
                        try {
                            JSONArray results = jsonObject.getJSONArray("results");
                            String key = results.getJSONObject(0).getString("key");
                            Intent intent = new Intent(MovieDetails.this, MovieTrailerActivity.class);
                            intent.putExtra("key", key);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                        Log.d(TAG, "onFailure");

                    }
                });
            }
        });

    }

}