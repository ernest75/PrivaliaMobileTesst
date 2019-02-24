package com.example.privaliamobiletest.sccreens.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.privaliamobiletest.R;
import com.example.privaliamobiletest.adapter.MoviesAdapter;
import com.example.privaliamobiletest.constants.Constants;
import com.example.privaliamobiletest.dagger.App;
import com.example.privaliamobiletest.networking.apimodels.MoviesResults;
import com.example.privaliamobiletest.networking.retroofitservice.MovieDBService;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private final String LOG_TAG = getClass().getSimpleName();

    @Inject
    MovieDBService mMovieDbService;

    private Call<MoviesResults> tvMovieCall;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((App) getApplication()).getApplicationComponent().inject(this);

        tvMovieCall = mMovieDbService.getPopularMovies(Constants.API_KEY,Constants.US_ENGLISH_LANGUAGE,1);
        tvMovieCall.enqueue(new Callback<MoviesResults>() {
            @Override
            public void onResponse(Call<MoviesResults> call, Response<MoviesResults> response) {
                Log.e(LOG_TAG,response.body().getResults().get(2).getOriginalTitle());
            }

            @Override
            public void onFailure(Call<MoviesResults> call, Throwable t) {
                Log.e(LOG_TAG,t.getMessage());
            }
        });

    }
}
