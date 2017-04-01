package com.splant.smartgarden;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.splant.smartgarden.ApiModel.ServerAPI;
import com.splant.smartgarden.beanModel.Entity.LocaleEnum;
import com.splant.smartgarden.beanModel.Responses.ClientInfoRes;
import com.splant.smartgarden.daggerModel.Component.AppComponent;
import com.splant.smartgarden.daggerModel.Component.DaggerAppComponent;
import com.splant.smartgarden.daggerModel.Module.AppModule;
import com.splant.smartgarden.utilModel.SpfManager;
import com.splant.smartgarden.weatherModel.Utils.DiskCacheController;
import com.splant.smartgarden.weatherModel.Utils.SimpleLocale;

import java.util.Locale;

/**
 * Created by aifengbin on 2017/3/17.
 */

public class SPlantApplication extends Application{

    public static String sUserId;
    public static ClientInfoRes sClientInfo;
    private static SPlantApplication mInstance;
    public static String sLocale = "zh-CN";
//    private GreenDaoManager greenDaoManager;

    public static SPlantApplication getInstance() {
        if (mInstance == null) {
            mInstance = new SPlantApplication();
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initLanguage();
        ServerAPI.getInstance().init(this);
        DiskCacheController.getInstance().init(this);
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(mInstance);
    }


    public AppComponent getAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule((SPlantApplication) getApplicationContext()))
                .build();
    }

    /**
     * 初始化 app 语言
     */
    private void initLanguage() {
        String localeString = SpfManager.getInstance().getLocale(this);
        Locale locale = Locale.getDefault();
        if (TextUtils.isEmpty(localeString)) {
            //没有设置，默认使用系统语言,如果不是英语语系，则为中文
            if ("en".equals(locale.getLanguage())) {
                sLocale = LocaleEnum.EN.getValue();
            } else {
                sLocale = LocaleEnum.CN.getValue();
            }
        } else {
            sLocale = localeString;
            if (!localeString.startsWith(locale.getLanguage())) {
                //设置和系统语言不一样，使用app设置语言
                setLocale(Locale.ENGLISH.getLanguage().equals(locale.getLanguage()) ? Locale.CHINESE : Locale.ENGLISH);
            }
        }
    }

    /**
     * 获取 app 语言区域
     */
    public static Locale getAppLocale() {
        if (sLocale.startsWith(Locale.CHINESE.getLanguage())) {
            return Locale.CHINESE;
        } else {
            return Locale.ENGLISH;
        }
    }

    /**
     * 设置 app 语言区域
     */
    public void setLocale(Locale locale) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
        SimpleLocale.getInstance().setLocale(locale);
    }

    /**
     * 获取客户id
     */
    public static String getClientId() {
        return sClientInfo == null ? null : sClientInfo.clientId;
    }

//    public void exit() {
//        ServerAPI.getInstance().unInit(this);
//    }

}
