package com.splant.smartgarden.weatherModel.Bean;


import com.splant.smartgarden.beanModel.Responses.WeatherRes;

import java.util.ArrayList;

/**
 * Created by aifengbin on 2017/3/16.
 */

public class WeatherDataWrapper {

    public WeatherRes.WeatherInfo mWeatherInfo;
    public ArrayList<WeatherRes.Humidity> mHumidities = new ArrayList<>(24);
    public ArrayList<WeatherRes.UV> mUVs = new ArrayList<>(24);
}
