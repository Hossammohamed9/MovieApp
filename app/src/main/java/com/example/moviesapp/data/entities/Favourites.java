package com.example.moviesapp.data.entities;

import static com.example.moviesapp.utils.Constants.FAVORITE_TABLE_NAME;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = FAVORITE_TABLE_NAME)
public class Favourites {

    @PrimaryKey
    private int key;

    public Favourites(int id){
        this.key = id;
    }

    public Favourites(){}

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Favourites that = (Favourites) o;
        return key == that.key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "Favourites{" +
                "key=" + key +
                '}';
    }


}
