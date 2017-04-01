package com.splant.smartgarden.weatherModel.Loader;

import android.content.Context;

import com.splant.smartgarden.ApiModel.ServerAPI;
import com.splant.smartgarden.beanModel.Responses.WeatherRes;
import com.splant.smartgarden.weatherModel.Utils.DiskCacheController;


/**
 * 天气
 * Created by aifengbin on 2017/3/20.
 */
public class WeatherLoader extends BaseLoader<WeatherRes> {

    public static final int LOAD_ID = 0x03;

    private String mLocale;
    private String mClientId;

    public WeatherLoader(Context context, String clientId, String locale) {
        super(context);
        mLocale = locale;
        mClientId = clientId;
    }

    @Override
    public WeatherRes loadInBackground() {
        try {
            WeatherRes res = ServerAPI.getInstance().getWeather(mClientId, mLocale);
            if (res != null && res.isSuccessed == 1) {
                DiskCacheController.getInstance().put("weather_cache_key", res);
                return res;
            } else {
                res = DiskCacheController.getInstance().get("weather_cache_key");
                return res;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}