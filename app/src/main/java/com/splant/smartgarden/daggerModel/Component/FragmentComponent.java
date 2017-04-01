package com.splant.smartgarden.daggerModel.Component;

import com.splant.smartgarden.baseModel.BaseFragment;
import com.splant.smartgarden.daggerModel.Module.FragmentModel;
import com.splant.smartgarden.daggerModel.Scope.ActivityScope;
import com.splant.smartgarden.uiModel.fragment.PlantFragment;
import com.splant.smartgarden.uiModel.fragment.PlantTypeFragment;
import com.splant.smartgarden.uiModel.fragment.ProfileFragment;
import com.splant.smartgarden.uiModel.fragment.WeatherFragment;

import dagger.Component;

/**
 * Created by aifengbin on 2017/4/1.
 */

@ActivityScope
@Component(dependencies = {AppComponent.class}, modules = {FragmentModel.class})
public interface FragmentComponent {

    void inject(BaseFragment baseFragment);

    void inject(PlantFragment plantFragment);

    void inject(PlantTypeFragment plantTypeFragment);

    void inject(ProfileFragment profileFragment);

    void inject(WeatherFragment weatherFragment);
}
