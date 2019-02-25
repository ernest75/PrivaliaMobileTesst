package com.example.privaliamobiletest.networking.retroofitservice;

import com.example.privaliamobiletest.networking.apimodels.Movie;
import com.example.privaliamobiletest.networking.apimodels.MoviesResults;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieDBService {

    @GET("movie/popular")
    Call<MoviesResults> getPopularMovies(@Query("api_key")String apiKey,@Query("language") String language, @Query("page") int page);

    @GET("movie/popular")
    Observable<MoviesResults> getPopularMoviesRx(@Query("api_key")String apiKey, @Query("language") String language, @Query("page") int page);


    @GET("search/movie")
    Observable<MoviesResults> getSearchMovies(@Query("api_key")String apiKey,@Query("query") String query, @Query("page") int page);


}
