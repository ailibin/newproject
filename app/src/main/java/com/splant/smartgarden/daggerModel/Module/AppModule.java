package com.splant.smartgarden.daggerModel.Module;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.splant.smartgarden.SPlantApplication;
import com.splant.smartgarden.daoModel.GreenDaoManager;
import com.splant.smartgarden.utilModel.SpfManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aifengbin on 2017/3/13.
 */

@Module
public class AppModule {

    private SPlantApplication mApplication;

    public AppModule(SPlantApplication application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    public Context provideApplication(){
        return mApplication;
    }

    @Provides
    @Singleton
    public SpfManager providePreference(){
        return new SpfManager(mApplication);
    }

    @Provides
    @Singleton
    public GreenDaoManager provideGreenDaoManager(){
        return new GreenDaoManager();
    }

    @Provides
    @Singleton
    public AsyncHttpClient provideClient(){
        return new AsyncHttpClient();
    }
}
