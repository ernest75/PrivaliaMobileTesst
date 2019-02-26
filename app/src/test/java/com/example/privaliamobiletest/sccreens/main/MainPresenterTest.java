package com.example.privaliamobiletest.sccreens.main;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.privaliamobiletest.constants.Constants;
import com.example.privaliamobiletest.networking.apimodels.Movie;
import com.example.privaliamobiletest.networking.retroofitservice.MovieDBService;

import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentCaptor.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {
    public static final String SOME_DATA = "some data";
    public static final String WRONG_DATA = "wrong data";

    // region constants ----------------------------------------------------------------------------

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------

    @Mock
    MainMVP.View mMainViewMocked;

    CompositeDisposable mCompositeDisposableMocked;

    @Mock
    MainMVP.Model mMainModelMocked;

    @Mock
    ArrayList<Movie> mMoviesArrayListMocked;

    // end region helper fields --------------------------------------------------------------------

   static MainPresenter SUT;

    @Before
    public void setup() throws Exception {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                new Function<Callable<Scheduler>, Scheduler>() {

                    @Override
                    public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                        return Schedulers.trampoline();
                    }
                });

        mCompositeDisposableMocked = new CompositeDisposable();

        SUT = new MainPresenter(mMainModelMocked);

        final Scheduler immediate = new Scheduler() {
            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(new Executor() {
                    @Override
                    public void execute(@NonNull Runnable runnable) {
                        runnable.run();
                    }
                });
            }
        };

        RxJavaPlugins.setInitIoSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> scheduler) throws Exception {
                return immediate;
            }
        });
        RxJavaPlugins.setInitComputationSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> scheduler) throws Exception {
                return immediate;
            }
        });
        RxJavaPlugins.setInitNewThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> scheduler) throws Exception {
                return immediate;
            }
        });
        RxJavaPlugins.setInitSingleSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> scheduler) throws Exception {
                return immediate;
            }
        });
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> scheduler) throws Exception {
                return immediate;
            }
        });

    }

    @Test
    public void correctViewAssigned_correctViewStored() throws Exception {
        // Arrange
        // Act
        SUT.setView(mMainViewMocked);
        // Assert
        assertThat(SUT.mView,is(mMainViewMocked));
    }

    @Test
    public void nullViewAssigned_noViewStored() throws Exception {
        // Arrange
        // Act
        SUT.setView(null);
        // Assert
        assertThat(SUT.mView,is(nullValue()));
    }

    @Test
    public void loadData_currentPageBiggerThan1_showProgressBarPagination() throws Exception {
        // Arrange
        SUT.setView(mMainViewMocked);
        when(SUT.mView.getCurrentServerPage()).thenReturn(2);
        when(SUT.mModel.getPopularMoviesFromServer(anyInt())).thenReturn(new Observable<Movie>() {
            @Override
            protected void subscribeActual(Observer<? super Movie> observer) {

            }
        });
        // Act
        SUT.loadData();
        // Assert
        verify(SUT.mView,times(1)).showProgressbarPagination();
    }

    @Test
    public void loadData_currentPageIs1_showBigProgressBarInvoked() throws Exception {
        // Arrange
        SUT.setView(mMainViewMocked);
        when(SUT.mView.getCurrentServerPage()).thenReturn(1);
        when(SUT.mModel.getPopularMoviesFromServer(anyInt())).thenReturn(new Observable<Movie>() {
            @Override
            protected void subscribeActual(Observer<? super Movie> observer) {

            }
        });
        // Act
        SUT.loadData();
        // Assert
        verify(SUT.mView,times(1)).showProgressbarBig();

    }

    @Test
    public void loadData_getPopularMoviesFromServerInvoked() throws Exception {
        // Arrange
        SUT.setView(mMainViewMocked);
        SUT.mModel = mMainModelMocked;
        when(SUT.mView.getCurrentServerPage()).thenReturn(10);
        when(SUT.mModel.getPopularMoviesFromServer(anyInt())).thenReturn(new Observable<Movie>() {
            @Override
            protected void subscribeActual(Observer<? super Movie> observer) {

            }
        });
        // Act
        SUT.loadData();
        // Assert
        verify(SUT.mModel,times(1)).getPopularMoviesFromServer(10);

    }

    @Test
    public void loadData_onNext_movieAddedToArray() throws Exception {
        // Arrange
        SUT.setView(mMainViewMocked);
        SUT.mModel = mMainModelMocked;
        when(SUT.mView.getCurrentServerPage()).thenReturn(10);
        Observable<Movie> movieObservable = Observable.just(new Movie());
        when(SUT.mModel.getPopularMoviesFromServer(anyInt())).thenReturn(movieObservable);
        // Act
        SUT.mMovies = mMoviesArrayListMocked;
        SUT.loadData();
        // Assert
        verify(SUT.mMovies,times(1)).add(any(Movie.class));

    }

    @Test
    public void loadData_onError_viewShowerrorWithRightMessage() throws Exception {
        // Arrange
        SUT.setView(mMainViewMocked);
        SUT.mModel = mMainModelMocked;
        when(SUT.mView.getCurrentServerPage()).thenReturn(10);
        Exception exception = new RuntimeException(WRONG_DATA);
        Observable<Movie> movieObservable = Observable.just(new Movie()).concatWith(Observable.<Movie>error(exception));
        when(SUT.mModel.getPopularMoviesFromServer(anyInt())).thenReturn(movieObservable);
        // Act
        SUT.loadData();
        // Assert
        verify(mMainViewMocked,times(1)).showErrorFromNetwork(WRONG_DATA);
    }

    @Test
    public void loadSearchedData_currentPageBiggerThan1_showProgressBarPagination() throws Exception {
        // Arrange
        SUT.setView(mMainViewMocked);
        when(SUT.mView.getCurrentServerPage()).thenReturn(2);
        when(SUT.mModel.getSearchedMovies(anyInt(),anyString())).thenReturn(new Observable<Movie>() {
            @Override
            protected void subscribeActual(Observer<? super Movie> observer) {

            }
        });
        // Act
        SUT.loadSearchedData(SOME_DATA);
        // Assert
        verify(SUT.mView,times(1)).showProgressbarPagination();
    }

    @Test
    public void loadSearchedData_currentPageIs1_showBigProgressBarInvoked() throws Exception {
        // Arrange
        SUT.setView(mMainViewMocked);
        when(SUT.mView.getCurrentServerPage()).thenReturn(1);
        when(SUT.mModel.getSearchedMovies(anyInt(),anyString())).thenReturn(new Observable<Movie>() {
            @Override
            protected void subscribeActual(Observer<? super Movie> observer) {

            }
        });
        // Act
        SUT.loadSearchedData(SOME_DATA);
        // Assert
        verify(SUT.mView,times(1)).showProgressbarBig();

    }

    @Test
    public void loadSearchedData_getPopularMoviesFromServerInvoked() throws Exception {
        // Arrange
        SUT.setView(mMainViewMocked);
        SUT.mModel = mMainModelMocked;
        when(SUT.mView.getCurrentServerPage()).thenReturn(10);
        when(SUT.mModel.getSearchedMovies(anyInt(),anyString())).thenReturn(new Observable<Movie>() {
            @Override
            protected void subscribeActual(Observer<? super Movie> observer) {

            }
        });
        // Act
        SUT.loadSearchedData(SOME_DATA);
        // Assert
        verify(SUT.mModel,times(1)).getSearchedMovies(anyInt(),anyString());

    }

    @Test
    public void loadSearchedData_onNext_movieAddedToArray() throws Exception {
        // Arrange
        SUT.setView(mMainViewMocked);
        SUT.mModel = mMainModelMocked;
        when(SUT.mView.getCurrentServerPage()).thenReturn(10);
        Observable<Movie> movieObservable = Observable.just(new Movie());
        when(SUT.mModel.getSearchedMovies(anyInt(),anyString())).thenReturn(movieObservable);
        // Act
        SUT.mMovies = mMoviesArrayListMocked;
        SUT.loadSearchedData(SOME_DATA);
        // Assert
        verify(SUT.mMovies,times(1)).add(any(Movie.class));

    }

    @Test
    public void loadSearchedData_onError_viewShowerrorWithRightMessage() throws Exception {
        // Arrange
        SUT.setView(mMainViewMocked);
        SUT.mModel = mMainModelMocked;
        when(SUT.mView.getCurrentServerPage()).thenReturn(10);
        Exception exception = new RuntimeException(WRONG_DATA);
        Observable<Movie> movieObservable = Observable.just(new Movie()).concatWith(Observable.<Movie>error(exception));
        when(SUT.mModel.getSearchedMovies(anyInt(),anyString())).thenReturn(movieObservable);
        // Act
        SUT.loadSearchedData(SOME_DATA);
        // Assert
        verify(mMainViewMocked,times(1)).showErrorFromNetwork(WRONG_DATA );
    }

    @Test
    public void handleOnComplete_moviesEmpty_viewShowNoResultsInvoked() throws Exception {
        // Arrange
        SUT.mMovies = new ArrayList<>(0);
        SUT.setView(mMainViewMocked);
        // Act
        SUT.handleOnComplete();
        // Assert
        verify(SUT.mView,times(1)).showNoResultsFromSearchMessage();
    }

    @Test
    public void handleOnComplete_moviesNotEmpty_viewDoesNotINvokeShowNoresults() throws Exception {
        // Arrange
        SUT.setView(mMainViewMocked);
        Movie movie = new Movie();
        // Act
        SUT.mMovies.add(movie);
        SUT.handleOnComplete();
        // Assert
        verify(SUT.mView,never()).showNoResultsFromSearchMessage();

    }

    @Test
    public void handleOnComplete_moviesNotEmpty_viewShowDataInvoked() throws Exception {
        // Arrange
        SUT.setView(mMainViewMocked);
        Movie movie = new Movie();
        // Act
        SUT.mMovies.add(movie);
        SUT.handleOnComplete();
        // Assert
        verify(SUT.mView,times(1)).showData(SUT.mMovies);
    }

    @Test
    public void handleOnComplete_moviesNotEmpty_viewHideProgressbarMethodsInvoked() throws Exception {
        // Arrange
        SUT.setView(mMainViewMocked);
        Movie movie = new Movie();
        // Act
        SUT.mMovies.add(movie);
        SUT.handleOnComplete();
        // Assert
        verify(SUT.mView,times(1)).hideProgressbarBig();
        verify(SUT.mView,times(1)).hideProgressbarPagination();
    }

    @Test
    public void handleOnComplete_moviesNotEmpty_viewSetLoadingInvoked() throws Exception {
        // Arrange
        SUT.setView(mMainViewMocked);
        Movie movie = new Movie();
        // Act
        SUT.mMovies.add(movie);
        SUT.handleOnComplete();
        // Assert
        verify(SUT.mView,times(1)).setLoadingToTrue();
    }

    @Test
    public void handleOnComplete_moviesNotEmpty_modelGetTotalPagesCurrentPetitionInvoked() throws Exception {
        // Arrange
        SUT.setView(mMainViewMocked);
        SUT.mModel = mMainModelMocked;
        Movie movie = new Movie();
        // Act
        SUT.mMovies.add(movie);
        SUT.handleOnComplete();
        // Assert
        verify(mMainModelMocked,times(1)).getTotalPagesCurrentPetition();
    }

    // region helper methods -----------------------------------------------------------------------

    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------


    // end region helper classes -------------------------------------------------------------------


}