package com.example.privaliamobiletest.sccreens.main;

import android.util.Log;

import com.example.privaliamobiletest.constants.Constants;
import com.example.privaliamobiletest.networking.apimodels.Movie;
import com.example.privaliamobiletest.networking.apimodels.MoviesResults;
import com.example.privaliamobiletest.networking.retroofitservice.MovieDBService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainModel implements MainMVP.Model {

    MovieDBService mMovieDbService;

    public MainModel(MovieDBService mMovieDbService) {
        this.mMovieDbService = mMovieDbService;
    }

    @Override
    public Observable<Movie> getPopularMoviesFromServer(int page) {

        Observable<Movie> popularMoviesObservable = mMovieDbService.getPopularMoviesRx(Constants.API_KEY, Constants.US_ENGLISH_LANGUAGE, page)
                .concatMap(new Function<MoviesResults, Observable<Movie>>() {
                    @Override
                    public Observable<Movie> apply(MoviesResults moviesResults) throws Exception {
                        return Observable.fromIterable(moviesResults.getResults());
                    }
                });
        return popularMoviesObservable;
    }
}
