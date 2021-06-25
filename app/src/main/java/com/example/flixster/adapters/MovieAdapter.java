package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.MovieDetails;
import com.example.flixster.R;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    static Context context;
    List<Movie> movies;
    ViewHolder.OnClickListener onClickListener;

    public MovieAdapter(Context context, List<Movie> movies, ViewHolder.OnClickListener onClickListener) {
        this.context = context;
        this.movies = movies;
        this.onClickListener = onClickListener;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView, onClickListener);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder " + position);
        // Get the movie at the passed position
        Movie movie = movies.get(position);
        // Bind the movie data into the view holder
        holder.bind(movie);

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        OnClickListener onClickListener;

        public ViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);

            this.onClickListener = onClickListener;
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText((movie.getTitle()));
            tvOverview.setText((movie.getOverview()));

            String imageURL;
            int placeholderOrientation;

            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageURL = movie.getBackdropPath();
                placeholderOrientation = R.mipmap.backdrop_placeholder_foreground;
            }
            else {
                imageURL = movie.getPosterPath();
                placeholderOrientation = R.mipmap.placeholder_foreground;
            }

            int radius = 30;
            int margin = 10;
            Glide.with(context)
                    .load(imageURL)
                    .placeholder(placeholderOrientation)
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivPoster);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onMovieClick(getAdapterPosition());
        }

        public interface OnClickListener{
            void onMovieClick(int position);
        }
    }
}
