package com.example.moviesapp.data.entities;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie implements Parcelable {


    private String overview;
    private String original_language;
    private String original_title;
    private boolean video;
    private boolean isFavorite;
    private String title;
    private String poster_path;
    private String backdrop_path;
    private String release_date;
    private double popularity;
    private float vote_average;
    @PrimaryKey()
    private int id;
    private boolean adult;
    private int vote_count;

    public Movie(String overview,
                 String original_language,
                 String original_title,
                 boolean video,
                 boolean isFavorite,
                 String title,
                 String poster_path,
                 String backdrop_path,
                 String release_date,
                 double popularity,
                 float vote_average,
                 int id,
                 boolean adult,
                 int vote_count) {

        this.overview = overview;
        this.original_language = original_language;
        this.original_title = original_title;
        this.video = video;
        this.isFavorite = isFavorite;
        this.title = title;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.release_date = release_date;
        this.popularity = popularity;
        this.vote_average = vote_average;
        this.id = id;
        this.adult = adult;
        this.vote_count = vote_count;

    }

    protected Movie(Parcel in) {
        overview = in.readString();
        original_language = in.readString();
        original_title = in.readString();
        video = in.readByte() != 0;
        isFavorite = in.readByte() !=0;
        title = in.readString();
        poster_path = in.readString();
        backdrop_path = in.readString();
        release_date = in.readString();
        popularity = in.readDouble();
        vote_average = in.readFloat();
        id = in.readInt();
        adult = in.readByte() != 0;
        vote_count = in.readInt();
    }


    public String getOverview() {
        return overview;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public boolean isVideo() {
        return video;
    }

    public String getTitle() {
        return title;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public double getPopularity() {
        return popularity;
    }

    public float getVote_average() {
        return vote_average;
    }

    public int getId() {
        return id;
    }

    public boolean isAdult() {
        return adult;
    }

    public int getVote_count() {
        return vote_count;
    }

    @Override
    public String toString() {
        return
                "ResultsItem{" +
                        "overview = '" + overview + '\'' +
                        ",original_language = '" + original_language + '\'' +
                        ",original_title = '" + original_title + '\'' +
                        ",video = '" + video + '\'' +
                        ",isFavorite = '" + isFavorite + '\'' +
                        ",title = '" + title + '\'' +
                        ",poster_path = '" + poster_path + '\'' +
                        ",backdrop_path = '" + backdrop_path + '\'' +
                        ",release_date = '" + release_date + '\'' +
                        ",popularity = '" + popularity + '\'' +
                        ",vote_average = '" + vote_average + '\'' +
                        ",id = '" + id + '\'' +
                        ",adult = '" + adult + '\'' +
                        ",vote_count = '" + vote_count + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(overview);
        parcel.writeString(original_language);
        parcel.writeString(original_title);
        parcel.writeByte((byte) (video ? 1 : 0));
        parcel.writeByte((byte) (isFavorite ? 1 : 0));
        parcel.writeString(title);
        parcel.writeString(poster_path);
        parcel.writeString(backdrop_path);
        parcel.writeString(release_date);
        parcel.writeDouble(popularity);
        parcel.writeFloat(vote_average);
        parcel.writeInt(id);
        parcel.writeByte((byte) (adult ? 1 : 0));
        parcel.writeInt(vote_count);
    }


    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}