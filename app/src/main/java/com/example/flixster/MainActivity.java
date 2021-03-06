package com.example.flixster;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ViewHolder.OnClickListener{

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=";
    public static final String TAG = "MainActivity";

    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ActionBar toolbar;
        toolbar = getSupportActionBar();
        toolbar.setTitle("Flixster \uD83C\uDF7F");

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#7a2121"));
        toolbar.setBackgroundDrawable(colorDrawable);

        movies = new ArrayList<>();

        // Create the adapter
        MovieAdapter movieAdapter = new MovieAdapter(this, movies, this);

        // Set the adapter on the recycler view
        binding.rvMovies.setAdapter(movieAdapter);

        // Set a Layout Manager on the recycler view
        binding.rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL + getString(R.string.IMDB_API_KEY), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    movies.addAll(Movie.fromJSONArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies: " + movies.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");

            }
        });
    }

    @Override
    public void onMovieClick(int position) {
        Movie movie = movies.get(position);
        Intent intent = new Intent(MainActivity.this, MovieDetails.class);
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("overview", movie.getOverview());
        intent.putExtra("poster", movie.getPosterPath());
        intent.putExtra("backdropPoster", movie.getBackdropPath());
        intent.putExtra("movieRating", movie.getVoteAverage());
        intent.putExtra("id", movie.getId());
        startActivity(intent);

    }
}