package com.example.privaliamobiletest.sccreens.main;

import com.example.privaliamobiletest.networking.apimodels.Movie;

import java.util.List;

import io.reactivex.Observable;

public interface MainMVP {

    interface Model{

        Observable<Movie> getPopularMoviesFromServer(int page);

    }

    interface Presenter{

        void setView(MainMVP.View view);

        void loadData();

        void rxJavaUnsubscribe();

    }

    interface View{

        void showData(List<Movie> mMovieList);

        int getCurrentServerPage();

        void showProgressbar();

        void hideProgressbar();

        void setLoadingToTrue();

        void setLoadingToFalse();

        void showErrorFromNetwork(String message);

        void showMessage(String s);

    }
}
