package com.example.privaliamobiletest.sccreens.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.privaliamobiletest.R;
import com.example.privaliamobiletest.adapter.MoviesAdapter;
import com.example.privaliamobiletest.constants.Constants;
import com.example.privaliamobiletest.dagger.App;
import com.example.privaliamobiletest.networking.apimodels.Movie;
import com.example.privaliamobiletest.networking.apimodels.MoviesResults;
import com.example.privaliamobiletest.networking.retroofitservice.MovieDBService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MainMVP.View {


    private final String LOG_TAG = getClass().getSimpleName();

    @Inject
    MovieDBService mMovieDbService;
    @Inject
    Context mContext;
    @Inject
    MainMVP.Presenter mPresenter;

    @BindView(R.id.etSearch)
    EditText mEtSearch;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.rvMovies)
    RecyclerView mRvMovies;


    private Call<MoviesResults> tvMovieCall;

    private MoviesAdapter mMoviesAdapter;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount, pageServer;

    private LinearLayoutManager mLayoutManager;

    private List<Movie> mMovieList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ((App) getApplication()).getApplicationComponent().inject(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        configView();

        mPresenter.setView(this);
        mPresenter.loadData();

    }

    private void configView() {
        mLayoutManager = new LinearLayoutManager(mContext);
        mRvMovies.setLayoutManager(mLayoutManager);
        mMoviesAdapter = new MoviesAdapter(mMovieList, mContext);
        mRvMovies.setAdapter(mMoviesAdapter);
        pageServer = 1;
        mRvMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            pageServer++;
                            Log.e(LOG_TAG,pageServer + "");
                            Log.e("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            mPresenter.loadData();
                            //getPopuarMoviesFromServer(pageServer);

                        }
                    }
                }
            }
        });

    }


    @Override
    public void showData(List<Movie> movieList) {
        mMovieList.addAll(movieList);
        mMoviesAdapter.notifyDataSetChanged();


    }

    @Override
    public int getCurrentServerPage() {
        return pageServer;
    }

    @Override
    public void showProgressbar() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideProgressbar() {
        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void setLoadingToTrue() {
        loading = true;
    }

    @Override
    public void setLoadingToFalse() {
        loading = false;
    }

    @Override
    public void showErrorFromNetwork(String message) {
        Toast.makeText(mContext,message,Toast.LENGTH_LONG).show();

    }

    @Override
    public void showMessage(String s) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.rxJavaUnsubscribe();
    }
}
