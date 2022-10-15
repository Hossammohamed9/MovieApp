package com.example.moviesapp.ui.movies;

import static com.example.moviesapp.utils.Constants.API_KEY;
import static com.example.moviesapp.utils.Constants.LANGUAGE;
import static com.example.moviesapp.utils.Constants.PAGE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.moviesapp.R;
import com.example.moviesapp.databinding.FragmentMoviesBinding;
import com.example.moviesapp.utils.ConnectionState;
import java.util.Objects;
import dagger.hilt.android.AndroidEntryPoint;
import io.github.muddz.styleabletoast.StyleableToast;

@AndroidEntryPoint
public class MoviesFragment extends Fragment {

    private FragmentMoviesBinding binding;
    private MovieListAdapter movieListAdapter;
    private MoviesViewModel moviesViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMoviesBinding.inflate(inflater, container, false);

        RecyclerView moviesRecyclerView = binding.moviesRecyclerView;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        moviesRecyclerView.setLayoutManager(gridLayoutManager);

        movieListAdapter = new MovieListAdapter(requireContext(), movie -> moviesViewModel.onMovieClicked(movie));
        moviesRecyclerView.setAdapter(movieListAdapter);

        moviesViewModel = new ViewModelProvider(this).get(MoviesViewModel.class);


        moviesViewModel.connectionState(requireContext().getApplicationContext()).observe(getViewLifecycleOwner(), s -> {
            if (s) {
                moviesViewModel.getAllMovies(API_KEY, LANGUAGE, PAGE);
                moviesViewModel.movieList.observe(getViewLifecycleOwner(), resultsItems -> {
                    if (resultsItems != null) {
                        movieListAdapter.setMovieList(resultsItems);
                        binding.setNoMoviesIsVisible(false);
                        binding.setRvIsVisible(true);
                    } else {
                        binding.setRvIsVisible(false);
                        binding.setNoMoviesIsVisible(true);

                    }
                });
            }
            else {
                moviesViewModel.getAllMoviesLocal();
                moviesViewModel.localMovies.observe(getViewLifecycleOwner(), movieList -> {
                    if (movieList != null) {
                        movieListAdapter.setMovieList(movieList);
                        binding.setNoMoviesIsVisible(false);
                        binding.setRvIsVisible(true);
                    } else {
                        binding.setNoMoviesIsVisible(true);
                        binding.setRvIsVisible(false);
                    }
                });
                makeConnectionLostToast();
            }
        });




        moviesViewModel.navigateToDetailsFragment.observe(getViewLifecycleOwner(), resultsItem -> {
            if (resultsItem == null)
                return;
            NavDirections action = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(resultsItem);
            Navigation.findNavController(requireView()).navigate(action);
            moviesViewModel.onDetailsFragmentNavigated();
        });

        binding.searchImage.setOnClickListener(view -> showSearchWithAnimation());

        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = charSequence.toString().toLowerCase();
                if (ConnectionState.isConnectedToInternet(requireContext())) {
                    moviesViewModel.filteredList.clear();
                    if (query.isEmpty()) {
                        moviesViewModel.filteredList.addAll(Objects.requireNonNull(moviesViewModel.movieList.getValue()));
                    } else {
                        moviesViewModel.search(query);
                        movieListAdapter.setMovieList(moviesViewModel.filteredList);
                        Objects.requireNonNull(binding.moviesRecyclerView.getAdapter()).notifyDataSetChanged();
                    }
                } else {
                    moviesViewModel.filteredOfflineList.clear();
                    if (query.isEmpty()) {
                        moviesViewModel.filteredOfflineList.addAll(Objects.requireNonNull(moviesViewModel.localMovies.getValue()));
                    } else {
                        moviesViewModel.search(query);
                        movieListAdapter.setMovieList(moviesViewModel.filteredOfflineList);
                        Objects.requireNonNull(binding.moviesRecyclerView.getAdapter()).notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.searchView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.searchView.setVisibility(View.INVISIBLE);
    }

    public void showSearchWithAnimation() {
        binding.searchView.setVisibility(View.VISIBLE);
        binding.searchView.setAlpha(0.0f);
        binding.searchView.animate()
                .translationY(30)
                .setDuration(600)
                .alpha(1.0f)
                .setListener(null);
    }

    private void makeConnectionLostToast() {
        StyleableToast.makeText(requireContext(), "Offline Data", Toast.LENGTH_SHORT, R.style.myInternetToast).show();
    }

}