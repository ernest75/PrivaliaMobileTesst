package com.example.privaliamobiletest.dagger;

import com.example.privaliamobiletest.networking.retroofitservice.MovieDBService;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.privaliamobiletest.constants.Constants.BASE_URL;

@Module
public class NetworkingModule {

    @Provides
    Retrofit getRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    MovieDBService getMovieDbApi(){
        return getRetrofit(BASE_URL).create(MovieDBService.class);
    }

}
