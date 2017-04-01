package com.splant.smartgarden.daggerModel.Component;

import com.loopj.android.http.AsyncHttpClient;
import com.splant.smartgarden.daggerModel.Module.AppModule;
import com.splant.smartgarden.daoModel.GreenDaoManager;
import com.splant.smartgarden.utilModel.SpfManager;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by aifengbin on 2017/3/13.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    SpfManager getSpfManager();

    GreenDaoManager getGreenDaoManager();

    AsyncHttpClient getAsyncHttpClient();

}
