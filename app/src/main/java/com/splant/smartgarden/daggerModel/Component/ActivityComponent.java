package com.splant.smartgarden.daggerModel.Component;

import android.app.Activity;

import com.splant.smartgarden.baseModel.BaseActivity;
import com.splant.smartgarden.daggerModel.Module.ActivityModule;
import com.splant.smartgarden.daggerModel.Scope.ActivityScope;
import com.splant.smartgarden.uiModel.activity.LoginActivity;
import com.splant.smartgarden.uiModel.activity.MainActivity;

import dagger.Component;

/**
 * Created by aifengbin on 2017/3/13.
 */

@ActivityScope
@Component(dependencies = {AppComponent.class}, modules = {ActivityModule.class})
public interface ActivityComponent {

    Activity getActivity();

    void inject(MainActivity mainActivity);

    void inject(LoginActivity loginActivity);

    void inject(BaseActivity baseActivity);
}
