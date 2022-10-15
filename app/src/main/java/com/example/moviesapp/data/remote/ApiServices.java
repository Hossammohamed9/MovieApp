package com.example.moviesapp.data.remote;

import com.example.moviesapp.data.entities.MovieResponse;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {

    @GET("movie/now_playing")
    Single<MovieResponse> getMoviesFromApi(@Query("api_key") String api_key,
                                           @Query("language") String lang,
                                           @Query("page") String page);

}
