package com.example.moviesapp.data.local;

import static androidx.room.OnConflictStrategy.REPLACE;
import static com.example.moviesapp.utils.Constants.FAVORITE_TABLE_NAME;
import static com.example.moviesapp.utils.Constants.TABLE_NAME;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.moviesapp.data.entities.Favourites;
import com.example.moviesapp.data.entities.Movie;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface UserDao {

    // ALL MOVIES

    @Insert(onConflict = REPLACE)
    Completable insert(List<Movie> movieList);

    @Query("SELECT * FROM " + TABLE_NAME)
    Single<List<Movie>> getAllMovies();

    //FAVORITE MOVIES

    @Insert(onConflict = REPLACE)
    Completable insertMovie(Favourites favoriteMovie);

    @Query("SELECT * FROM " + FAVORITE_TABLE_NAME)
    Observable<List<Favourites>> getAllFavorites();

    @Query("DELETE FROM " +  FAVORITE_TABLE_NAME + " WHERE `key`LIKE:id")
    Completable deleteMovieFromFavorite(int id);


}
