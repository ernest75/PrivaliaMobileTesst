package com.example.privaliamobiletest.networking.retroofitservice;

import com.example.privaliamobiletest.networking.apimodels.Movie;
import com.example.privaliamobiletest.networking.apimodels.MoviesResults;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieDBService {

    @GET("movie/popular")
    Call<MoviesResults> getPopularMovies(@Query("api_key")String apiKey,@Query("language") String language, @Query("page") int page);


//    @GET("search/movie")
//    Call<List<Movie>> getSearchMovies(@Query("query") String query, @Query("page") int page);


}
