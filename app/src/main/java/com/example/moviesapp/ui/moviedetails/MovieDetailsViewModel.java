package com.example.moviesapp.ui.moviedetails;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.example.moviesapp.data.entities.Movie;
import com.example.moviesapp.data.repository.Repository;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class MovieDetailsViewModel extends ViewModel {
    private static final String TAG = "MovieDetailsViewModel";

    private final Repository repository;


    @Inject
    public MovieDetailsViewModel(Repository repository) {

        this.repository = repository;
    }

    public void addToFavorite(Movie movie){
        repository.addMovie(movie).subscribeOn(Schedulers.computation())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }
                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: added to favourite");
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                    }
                });
    }

    public void deleteFavoriteMovie(int id){
        repository.deleteFavorite(id).subscribeOn(Schedulers.computation())
                .subscribe();
    }

    public String formatYear(String year){
        return "("+ year.substring(0,4)+")";
    }
}