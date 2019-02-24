package com.example.privaliamobiletest.dagger;


import com.example.privaliamobiletest.sccreens.main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class,NetworkingModule.class})
public interface ApplicationComponent {

    void inject(MainActivity mainActivity);
}
