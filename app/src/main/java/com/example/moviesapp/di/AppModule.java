package com.example.moviesapp.di;

import static com.example.moviesapp.utils.Constants.BASE_URL;
import static com.example.moviesapp.utils.Constants.DATABASE_NAME;

import android.app.Application;
import androidx.room.Room;
import com.example.moviesapp.data.local.RoomDB;
import com.example.moviesapp.data.local.UserDao;
import com.example.moviesapp.data.remote.ApiServices;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Singleton
    @Provides
    public ApiServices provideApiServices(Retrofit retrofit){
        return retrofit.create(ApiServices.class);
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofitInstance(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    public static RoomDB provideRoomInstance(Application application){
        return  Room.databaseBuilder(application, RoomDB.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    public static UserDao provideDao(RoomDB roomDB){
        return roomDB.userDao();
    }
}
