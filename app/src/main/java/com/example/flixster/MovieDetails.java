package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MovieDetails extends AppCompatActivity {

    TextView tvTitleDesc;
    TextView tvOverviewDesc;
    ImageView ivPosterDesc;
    RatingBar rbMovieRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);
        tvTitleDesc = findViewById(R.id.tvTitleDesc);
        tvOverviewDesc = findViewById(R.id.tvOverviewDesc);
        ivPosterDesc = findViewById(R.id.ivPosterDesc);
        rbMovieRating = findViewById(R.id.rbMovieRating);

        Intent i = getIntent();
        String title = i.getStringExtra("title");
        String overview = i.getStringExtra("overview");
        float voteAverage = (float) i.getDoubleExtra("movieRating", 0);

        // Change poster type based on portrait or landscape orientation
        String poster;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            poster = i.getStringExtra("backdropPoster");
        }
        else {
            poster = i.getStringExtra("poster");
        }

        tvTitleDesc.setText(title);
        tvOverviewDesc.setText(overview);
        rbMovieRating.setRating(voteAverage / 2.0f);

        Glide.with(this)
                .load(poster)
                .into(ivPosterDesc);

    }
}