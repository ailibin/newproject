package com.splant.smartgarden.daggerModel.Module;

import android.support.v4.app.Fragment;

import com.splant.smartgarden.daggerModel.Scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/4/1.
 */

@Module
public class FragmentModel {

    private Fragment fragment;

    public FragmentModel(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @ActivityScope
    Fragment fragment() {
        return this.fragment;
    }

}
