package com.example.moviesapp.ui.moviedetails;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moviesapp.R;
import com.example.moviesapp.data.entities.Movie;
import com.example.moviesapp.databinding.FragmentMovieDetailsBinding;
import dagger.hilt.android.AndroidEntryPoint;
import io.github.muddz.styleabletoast.StyleableToast;

@AndroidEntryPoint
public class MovieDetailsFragment extends Fragment {

    private Movie movie;
    private MovieDetailsViewModel movieDetailsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentMovieDetailsBinding binding = FragmentMovieDetailsBinding.inflate(inflater, container, false);

        movieDetailsViewModel = new ViewModelProvider(this).get(MovieDetailsViewModel.class);


        movie = MovieDetailsFragmentArgs.fromBundle(requireArguments()).getCurrentMovie();
        binding.setMovie(movie);

        String dateYear = movieDetailsViewModel.formatYear(movie.getRelease_date());
        binding.movieDetailsYear.setText(dateYear);
        Glide.with(requireContext())
                .load("https://image.tmdb.org/t/p/w500"+movie.getPoster_path())
                .apply(RequestOptions.centerCropTransform())
                .into(binding.movieDetailsPoster);

        binding.backArrow.setOnClickListener(view -> Navigation.findNavController(view).popBackStack());

        binding.markMovie.setOnClickListener(view -> {
            if (movie.isFavorite()){
                movieDetailsViewModel.deleteFavoriteMovie(movie.getId());
                binding.markMovie.setImageResource(R.drawable.white_bookmark);
                makeDeleteToast();
            }else {
                movieDetailsViewModel.addToFavorite(movie);
                binding.markMovie.setImageResource(R.drawable.checked_mark);
                makeAddToast();
            }
        });
        if (movie.isFavorite()){
            binding.markMovie.setImageResource(R.drawable.checked_mark);
        }

        return binding.getRoot();
    }

    private void makeAddToast(){
        StyleableToast.makeText(requireContext(), "Added to favorite", Toast.LENGTH_SHORT, R.style.mySuccessToast).show();
    }

    private void makeDeleteToast(){
        StyleableToast.makeText(requireContext(), "Removed from favorite", Toast.LENGTH_SHORT, R.style.myDeleteToast).show();
    }

}