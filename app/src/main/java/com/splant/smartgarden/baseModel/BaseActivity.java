package com.splant.smartgarden.baseModel;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.splant.smartgarden.R;
import com.splant.smartgarden.SPlantApplication;
import com.splant.smartgarden.beanModel.config.Constant;
import com.splant.smartgarden.customModel.TitlebarView;
import com.splant.smartgarden.daggerModel.Component.ActivityComponent;
import com.splant.smartgarden.daggerModel.Component.DaggerActivityComponent;
import com.splant.smartgarden.daggerModel.Module.ActivityModule;
import com.splant.smartgarden.utilModel.AppManager;
import com.splant.smartgarden.utilModel.SpfManager;
import com.splant.smartgarden.utilModel.ToastUtil;
import com.splant.smartgarden.utilModel.UtilsTool;
import com.zhy.autolayout.AutoLayoutActivity;

import java.lang.ref.WeakReference;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by aifengbin on 2017/3/8.
 */

public class BaseActivity extends AutoLayoutActivity implements BaseView{

    public TitlebarView titlebarView;
    protected String Tag = "activity";
    private final int DELAY_TIME = 500;

    @Inject
    public SpfManager spUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //把当前的activity添加到栈中
        Log.i(Tag, "Activity: " + getClass().getSimpleName());
        getActivityComponent().inject(this);
        AppManager.addActivity(this);
        //TODO 2016.7.15 根据上次的语言设置，重新设置语言
        switchLanguage(spUtils.getString("language", "zh"));
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        LinearLayout mMainLayout = new LinearLayout(this);
        mMainLayout.setOrientation(LinearLayout.VERTICAL);

        //标题栏的布局
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup.LayoutParams iabParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titlebarView = new TitlebarView(this);
        mMainLayout.addView(titlebarView, iabParams);

        //内容栏的布局
        View mContentView = mInflater.inflate(layoutResID, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mMainLayout.addView(mContentView, params);
        setContentView(mMainLayout);
        //绑定view
        ButterKnife.bind(this);
    }

    /**
     * 得到activity类的注解器,如果需要activityComponent里面的引用，
     * 可以类似这样“getActivityComponent.inject(this)”
     * @return
     */
    public ActivityComponent getActivityComponent(){
        return  DaggerActivityComponent.builder()
                .appComponent(SPlantApplication.getInstance().getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }
    public ActivityModule getActivityModule(){
        return new ActivityModule(this);
    }

    public static String getClientIPs() {//2016.12.5 B端二期没有外网了
        String localIP = SpfManager.getInstance().getString(Constant.SpfManagerParams.LocalServerIP,"120.77.53.132:3006");//120.77.53.132:3006是二期的外网地址
        return localIP;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //activity退出后弹栈
        AppManager.pop(this);
    }

    @Override
    public void Jump2Activity(Class ActivityClass) {
        Intent intent = new Intent(this, ActivityClass);
        startActivity(intent);
    }

    @Override
    public void ShowToast(int msgId) {
        ToastUtil.toastShort(this, msgId);
    }

    /**
     * activity的延时消失
     */
    protected void finishDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, DELAY_TIME);
    }

    /**
     * 设置返回按钮事件
     */
    protected void titiltBarSetLeftFinished(){
        titlebarView.setBtnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_static,R.anim.slide_right_out);
            }
        });
    }

    public static class UIHandler extends Handler {
        private WeakReference<BaseActivity> mRef;

        public UIHandler(BaseActivity baseFragment) {
            mRef = new WeakReference<BaseActivity>(baseFragment);
        }
        @Override
        public void handleMessage(Message msg) {
            BaseActivity activity = mRef.get();
            if (activity != null) {
                activity.onHandleMessage(activity, msg);
            }
        }
    }
    protected void onHandleMessage(BaseActivity baseActivity, Message message) { }

    //重新自定义语言的方法
    protected void switchLanguage(String language) {
        //设置应用语言类型
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (language.equals("en")) {
            config.locale = Locale.ENGLISH;
        } else {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }
        resources.updateConfiguration(config, dm);

        //保存设置语言的类型
        spUtils.saveString("language", language);
    }

    public String getLanguage(){
        return UtilsTool.changeLanguage(this,spUtils.getString(Constant.SpfManagerParams.LOCAL_LANGUAGE, "1"));
    }
}
