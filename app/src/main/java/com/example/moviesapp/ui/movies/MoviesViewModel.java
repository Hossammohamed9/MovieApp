package com.example.moviesapp.ui.movies;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.moviesapp.data.entities.Movie;
import com.example.moviesapp.data.repository.Repository;
import com.example.moviesapp.utils.ConnectionState;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


@HiltViewModel
public class MoviesViewModel extends ViewModel {

    private static final String TAG = "MoviesViewModel";

    private final MutableLiveData<List<Movie>> _moviesList;
    public LiveData<List<Movie>> movieList;

    private final MutableLiveData<Movie> _navigateToDetailsFragment;
    public LiveData<Movie> navigateToDetailsFragment;

    private final MutableLiveData<List<Movie>> _localMovies;
    public LiveData<List<Movie>> localMovies;

    private final Repository repository;
    private final Application application;

    public List<Movie> filteredList = new ArrayList<>();
    public List<Movie> filteredOfflineList = new ArrayList<>();

    public MutableLiveData<Boolean> messageVm;


    @Inject
    public MoviesViewModel(Repository repository, Application application) {
        _moviesList = new MutableLiveData<>();
        movieList = getMovieListObserver();
        _navigateToDetailsFragment = new MutableLiveData<>();
        navigateToDetailsFragment = navigateToDetailsFragment();
        _localMovies = new MutableLiveData<>();
        localMovies = get_localMovies();

        this.repository = repository;
        this.application = application;
        messageVm = new MutableLiveData<>(true);
        ConnectionState.isOnline = "true";

    }

    private MutableLiveData<List<Movie>> getMovieListObserver() {
        return _moviesList;
    }

    public MutableLiveData<Movie> navigateToDetailsFragment() {
        return _navigateToDetailsFragment;
    }

    public MutableLiveData<List<Movie>> get_localMovies() {
        return _localMovies;
    }

    @SuppressLint("CheckResult")
    public void getAllMovies(String api_key, String language, String page) {
        repository.getMoviesFromApi(api_key, language, page)
                .subscribeOn(Schedulers.io())
                .map(movieResponse -> {
                    return movieResponse.getResults();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(_moviesList::setValue,
                        error -> Log.e(TAG, "getAllMovies: " + error.getMessage()));
    }


    public void getAllMoviesLocal() {
        repository.getAllMovies().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Movie>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull List<Movie> movieList) {
                        _localMovies.setValue(movieList);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }
                });
    }

    public void onMovieClicked(Movie movie) {
        _navigateToDetailsFragment.setValue(movie);
    }

    public void onDetailsFragmentNavigated() {
        _navigateToDetailsFragment.setValue(null);
    }

    public void search(String query) {
        if (ConnectionState.isConnectedToInternet(application)) {
            for (Movie movie : Objects.requireNonNull(_moviesList.getValue())) {
                if (movie.getTitle().toLowerCase(Locale.getDefault()).contains(query)) {
                    filteredList.add(movie);
                }
            }
        } else {
            for (Movie movie : Objects.requireNonNull(_localMovies.getValue())) {
                if (movie.getTitle().toLowerCase(Locale.getDefault()).contains(query)) {
                    filteredOfflineList.add(movie);
                }
            }
        }

    }

    public LiveData<Boolean> connectionState(Context context) {

            ConnectionState.realTimeConnectionState(context, new Handler(Looper.getMainLooper(), message -> {
                        messageVm.postValue((boolean) message.obj);
                return false;
            }));

        return messageVm;
    }
}