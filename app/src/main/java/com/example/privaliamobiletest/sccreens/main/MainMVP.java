package com.example.privaliamobiletest.sccreens.main;

import com.example.privaliamobiletest.networking.apimodels.Movie;

import java.util.List;

import io.reactivex.Observable;

public interface MainMVP {

    interface Model{

        Observable<Movie> getPopularMoviesFromServer(int page);

        Observable<Movie> getSearchedMovies(int page, String query);

        int getTotalPagesCurrentPetition();

    }

    interface Presenter{

        void setView(MainMVP.View view);

        void loadData();

        void rxJavaUnsubscribe();

        void loadSearchedData(CharSequence query);

        int getTotalPagesCurrentPetition();

        int incrementPageServer();

        int resetPageServer();

        int getCurrentPagerServer();

    }

    interface View{

        void showData(List<Movie> mMovieList);

        void showProgressbarPagination();

        void hideProgressbarPagination();

        void showProgressbarBig();

        void hideProgressbarBig();

        void setLoadingToTrue();

        void setLoadingToFalse();

        void showErrorFromNetwork(String message);

        void showNoResultsFromSearchMessage();


    }
}
