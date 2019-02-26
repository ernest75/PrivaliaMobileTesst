package com.example.privaliamobiletest.sccreens.main;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.privaliamobiletest.constants.Constants;
import com.example.privaliamobiletest.networking.apimodels.Movie;
import com.example.privaliamobiletest.networking.apimodels.MoviesResults;
import com.example.privaliamobiletest.networking.retroofitservice.MovieDBService;
import com.google.gson.Gson;

import static android.app.SearchManager.QUERY;
import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.CoreMatchers.is;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subscribers.TestSubscriber;
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
public class MainModelTest {
    public static final int PAGE = 1;
    public static final String QUERY = "query";

    // region constants ----------------------------------------------------------------------------

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------

    MockWebServer mMockWebServer;

    MovieDBService mMovieDbService;
    // end region helper fields --------------------------------------------------------------------

    MainModel SUT;

    @Before
    public void setup() throws Exception {

        mMockWebServer = new MockWebServer();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mMockWebServer.url(Constants.BASE_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mMockWebServer.enqueue(new MockResponse().setBody(Constants.JSON_RESPONSE));
        mMovieDbService = retrofit.create(MovieDBService.class);

        SUT = new MainModel(mMovieDbService);

    }

    @Test
    public void getPopularMoviesFromServer_correctPagePassed_returnsCorrectObservable() throws Exception {
        // Arrange
        // Act
        Observable<Movie> result = SUT.getPopularMoviesFromServer(PAGE);
        // Assert
        //result.test().assertValueAt(0,any(Movie.class));
        assertThat(result,is(notNullValue()));

    }

    @Test
    public void getPopularMoviesFromServer_emitItems() throws Exception {
        // Arrange

        // Act
        Observable<Movie> result = SUT.getPopularMoviesFromServer(PAGE);
        // Assert
        assertThat(result.elementAt(0),is(notNullValue()));

    }

    @Test
    public void getPopularMoviesFromServer_emitsNoErrors() throws Exception {
        // Arrange
        TestSubscriber<Movie> subscriber = new TestSubscriber<>();
        // Act
        Observable<Movie> result = SUT.getPopularMoviesFromServer(PAGE);

        // Assert
        subscriber.assertNoErrors();

    }

    @Test
    public void getSearchedMovies_correctPagePassed_returnsCorrectObservable() throws Exception {
        // Arrange
        // Act
        Observable<Movie> result = SUT.getSearchedMovies(PAGE, QUERY);
        // Assert
        assertThat(result,is(notNullValue()));

    }

    @Test
    public void getSearchedMovies_emitItems() throws Exception {
        // Arrange

        // Act
        Observable<Movie> result = SUT.getSearchedMovies(PAGE, QUERY);
        // Assert
        assertThat(result.elementAt(0),is(notNullValue()));
    }

    @Test
    public void getSearchedMovies_emitsNoErrors() throws Exception {
        // Arrange
        TestSubscriber<Movie> subscriber = new TestSubscriber<>();
        // Act
        Observable<Movie> result = SUT.getSearchedMovies(PAGE, QUERY);

        // Assert
        subscriber.assertNoErrors();

    }

    @Test
    public void getTotalPagesCurrentPetition_returnsCorrectData() throws Exception {
        // Arrange
        SUT.mTotalPagesCurrentPetition = PAGE;
        // Act
        int result = SUT.getTotalPagesCurrentPetition();
        // Assert
        assertThat(result, is(PAGE));
    }

    // region helper methods -----------------------------------------------------------------------

    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // end region helper classes -------------------------------------------------------------------


}