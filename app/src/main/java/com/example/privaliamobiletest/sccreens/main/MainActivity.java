package com.example.privaliamobiletest.sccreens.main;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.privaliamobiletest.R;
import com.example.privaliamobiletest.adapter.MoviesAdapter;
import com.example.privaliamobiletest.dagger.App;
import com.example.privaliamobiletest.networking.apimodels.Movie;
import com.example.privaliamobiletest.networking.retroofitservice.MovieDBService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainMVP.View {

    private final String LOG_TAG = getClass().getSimpleName();

    //Dagger
    @Inject
    MovieDBService mMovieDbService;
    @Inject
    Context mContext;
    @Inject
    MainMVP.Presenter mPresenter;

    //ButterKnife
    @BindView(R.id.etSearch)
    EditText mEtSearch;
    @BindView(R.id.progressBarPagination)
    ProgressBar mProgressBarPagination;
    @BindView(R.id.rvMovies)
    RecyclerView mRvMovies;
    @BindView(R.id.progressBarBig)
    ProgressBar mProgressBarBig;
    @BindView(R.id.tvNoResultsFromSearch)
    TextView mTvNoResultsFromSearch;
    @BindView(R.id.mainLayout)
    ConstraintLayout mMainLayout;

    private MoviesAdapter mMoviesAdapter;

    private boolean loading = true;
    private boolean isSearch;
    int pastVisiblesItems, visibleItemCount, totalItemCount, pageServer;
    private CharSequence mCharSequenceSearch;

    private LinearLayoutManager mLayoutManager;

    private List<Movie> mMovieList = new ArrayList<>();


    //lifecycle methods
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.rxJavaUnsubscribe();
        mPresenter.setView(null);
    }

    @Override
    public void onBackPressed() {
        if (isSearch) {
            resetSearchFields();
        } else {

            super.onBackPressed();
        }

    }

    //mvp methods
    @Override
    public void showData(List<Movie> movieList) {
        mMovieList.addAll(movieList);
        mMoviesAdapter.notifyDataSetChanged();
        if (mMoviesAdapter.getItemCount() == 0) {
            mTvNoResultsFromSearch.setVisibility(View.VISIBLE);
        } else {
            mTvNoResultsFromSearch.setText(R.string.no_results_to_show_message);
            mTvNoResultsFromSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public int getCurrentServerPage() {
        return pageServer;
    }

    @Override
    public void showProgressbarPagination() {
        mProgressBarPagination.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideProgressbarPagination() {
        mProgressBarPagination.setVisibility(View.GONE);

    }

    @Override
    public void showProgressbarBig() {
        mProgressBarBig.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbarBig() {
        mProgressBarBig.setVisibility(View.GONE);
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
        Snackbar.make(mMainLayout, message, Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void showNoResultsFromSearchMessage() {
        mTvNoResultsFromSearch.setText(getString(R.string.no_results_from_search_1) + mCharSequenceSearch + getString(R.string.no_results_from_search_2));
        mTvNoResultsFromSearch.setVisibility(View.VISIBLE);
    }
    //class methods
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
                            if (pageServer < mPresenter.getTotalPagesCurrentPetition()) {
                                if (isSearch) {
                                    mPresenter.loadSearchedData(mCharSequenceSearch);
                                } else {
                                    mPresenter.loadData();
                                }

                            }
                            Log.e(LOG_TAG, pageServer + "");
                            Log.e("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data

                        }
                    } else {
                        //inform user no more results
                        if (pageServer == mPresenter.getTotalPagesCurrentPetition() && !mRvMovies.canScrollVertically(1)) {
                            Snackbar.make(mMainLayout, R.string.no_more_results_to_show, Snackbar.LENGTH_LONG).show();

                        }
                    }

                }
            }
        });

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mTvNoResultsFromSearch.setVisibility(View.GONE);
                mMovieList.clear();
                mMoviesAdapter.notifyDataSetChanged();
                if (charSequence.length() > 0) {
                    mPresenter.loadSearchedData(charSequence);
                    mCharSequenceSearch = charSequence;
                } else {
                    mTvNoResultsFromSearch.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                isSearch = true;
                pageServer = 1;

            }
        });

    }

    private void resetSearchFields() {
        mEtSearch.setText("");
        mCharSequenceSearch = null;
        isSearch = false;
        pageServer = 1;
        mMovieList.clear();
        mMoviesAdapter.notifyDataSetChanged();
        mPresenter.loadData();
        mTvNoResultsFromSearch.setText(" ");
    }
}
