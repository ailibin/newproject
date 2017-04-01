package com.splant.smartgarden.daggerModel.Module;

import android.app.Activity;

import com.splant.smartgarden.daggerModel.Scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aifengbin on 2017/3/13.
 */

@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    Activity activity() {
        return this.activity;
    }
}
