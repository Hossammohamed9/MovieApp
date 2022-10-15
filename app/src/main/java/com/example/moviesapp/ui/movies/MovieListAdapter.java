package com.example.moviesapp.ui.movies;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moviesapp.data.entities.Movie;
import com.example.moviesapp.databinding.MovieItemBinding;
import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private Context context;
    private List<Movie> movieList;
    private ItemClickListener clickListener;

    public MovieListAdapter(Context context, ItemClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView movieImage;
        TextView movieName;
        TextView movieDescription;
        TextView movieYear;
        RatingBar movieRating;
        MovieItemBinding binding;

        public ViewHolder(MovieItemBinding binding) {
            super(binding.getRoot());
            movieImage = binding.movieImage;
            movieName = binding.movieName;
            movieDescription = binding.movieDescription;
            movieYear = binding.movieYear;
            movieRating = binding.ratingBar;
            this.binding = binding;
        }

    }

    @NonNull
    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MovieItemBinding view = MovieItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.ViewHolder holder, int position) {
        Movie movie = this.movieList.get(position);

        if (movie.isFavorite()){
            holder.binding.favorite.setVisibility(View.VISIBLE);
        }else {
            holder.binding.favorite.setVisibility(View.INVISIBLE);
        }
        String dateYear = "("+ movie.getRelease_date().substring(0,4)+")";
        holder.itemView.setOnClickListener(view -> clickListener.onItemClick(movie));

        holder.movieName.setText(movie.getTitle());
        holder.movieDescription.setText(movie.getOverview());
        holder.movieYear.setText(dateYear);
        holder.movieRating.setRating(movie.getVote_average()/2);
        LayerDrawable stars = (LayerDrawable) holder.movieRating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500"+movie.getPoster_path())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.movieImage);

    }

    @Override
    public int getItemCount() {
        if (this.movieList != null) {
            return this.movieList.size();
        }
        return 0;
    }

    public interface ItemClickListener{
        public void onItemClick(Movie movie);
    }
}
