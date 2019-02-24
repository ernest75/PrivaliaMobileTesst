package com.example.privaliamobiletest.dagger;

import android.app.Application;
import android.content.Context;

import com.example.privaliamobiletest.networking.retroofitservice.MovieDBService;
import com.example.privaliamobiletest.sccreens.main.MainMVP;
import com.example.privaliamobiletest.sccreens.main.MainModel;
import com.example.privaliamobiletest.sccreens.main.MainPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application mApplication;

    public ApplicationModule(Application application){
        mApplication = application;
    }

    @Provides
    @Singleton
    Context provideContext(){
        return mApplication;
    }

    @Provides
    MainMVP.Presenter provideMainPresenter(MovieDBService movieDBService){
        return new MainPresenter(provideMainModel(movieDBService));
    }

    @Provides
    MainMVP.Model provideMainModel(MovieDBService movieDbService){
        return new MainModel(movieDbService);
    }
}
