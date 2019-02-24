package com.example.privaliamobiletest.sccreens.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.privaliamobiletest.R;
import com.example.privaliamobiletest.adapter.MoviesAdapter;
import com.example.privaliamobiletest.constants.Constants;
import com.example.privaliamobiletest.dagger.App;
import com.example.privaliamobiletest.networking.apimodels.MoviesResults;
import com.example.privaliamobiletest.networking.retroofitservice.MovieDBService;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private final String LOG_TAG = getClass().getSimpleName();

    @Inject
    MovieDBService mMovieDbService;

    @Inject
    Context mContext;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.rvMovies)
    RecyclerView rvMovies;

    private Call<MoviesResults> tvMovieCall;

    private MoviesAdapter mMoviesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ((App) getApplication()).getApplicationComponent().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        tvMovieCall = mMovieDbService.getPopularMovies(Constants.API_KEY, Constants.US_ENGLISH_LANGUAGE, 1);
        tvMovieCall.enqueue(new Callback<MoviesResults>() {
            @Override
            public void onResponse(Call<MoviesResults> call, Response<MoviesResults> response) {

                Log.e(LOG_TAG, response.body().getResults().get(2).getOriginalTitle());
                mMoviesAdapter = new MoviesAdapter(response.body().getResults(), mContext);
                rvMovies.setAdapter(mMoviesAdapter);
                rvMovies.setLayoutManager(new LinearLayoutManager(mContext));

            }

            @Override
            public void onFailure(Call<MoviesResults> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });

    }
}
