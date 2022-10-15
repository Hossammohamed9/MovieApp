package com.example.moviesapp.data.repository;

import android.util.Log;

import com.example.moviesapp.data.entities.Favourites;
import com.example.moviesapp.data.entities.Movie;
import com.example.moviesapp.data.entities.MovieResponse;
import com.example.moviesapp.data.local.RoomDB;
import com.example.moviesapp.data.remote.ApiServices;
import java.util.List;
import javax.inject.Inject;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Repository {
    private static final String TAG = "Repository";

    private ApiServices apiServices;
    private RoomDB database;

    @Inject
    public Repository(ApiServices apiServices, RoomDB database) {
        this.apiServices = apiServices;
        this.database = database;
    }

    //RETROFIT FUNCTIONS

    public Observable<MovieResponse> getMoviesFromApi(String key, String lang, String page){
        Observable<MovieResponse> x = Observable.create(emitter -> {
            MovieResponse movies = apiServices.getMoviesFromApi(key, lang, page).blockingGet();
            database.userDao().getAllFavorites().map(favouritesList -> {
                for (Movie m : movies.getResults()) {
                    m.setFavorite(favouritesList.contains(new Favourites(m.getId())));
                }
                return movies;
            }).subscribe(new Observer<MovieResponse>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                }

                @Override
                public void onNext(@NonNull MovieResponse movieResponse) {
                    if (movieResponse != null) {
                        database.userDao().insert(movieResponse.getResults())
                                .subscribeOn(Schedulers.computation())
                                .subscribe();
                    }
                    emitter.onNext(movieResponse);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                }

                @Override
                public void onComplete() {
                }
            });
        });

         return x;
    }

    //DATABASE FUNCTIONS

    public Single<List<Movie>> getAllMovies(){
        return database.userDao().getAllMovies();
    }

    public Completable addMovie(Movie movie){
        return database.userDao().insertMovie(new Favourites(movie.getId()));
    }


    public Completable deleteFavorite(int id){
        return database.userDao().deleteMovieFromFavorite(id);
    }


}
