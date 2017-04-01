package com.splant.smartgarden.uiModel.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.splant.smartgarden.R;
import com.splant.smartgarden.baseModel.BaseActivity;
import com.splant.smartgarden.utilModel.ToastUtil;

import butterknife.Bind;
import butterknife.OnClick;

import static com.splant.smartgarden.R.id.rb_district_version;


/**
 * 欢迎界面的版本选择
 */
public class SplashActivity extends BaseActivity {

    @Bind(rb_district_version)
    RadioButton RbDistrictVersion;
    @Bind(R.id.rb_municipal_version)
    RadioButton RbMunicipalVersion;
    @Bind(R.id.rg_select_version)
    RadioGroup RgSelectVersion;
    @Bind(R.id.splash_btn_sure)
    Button BtnSure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //默认选择小区版
        RbDistrictVersion.setChecked(true);
        titlebarView.setTitleBarDismiss(true);
//        int screenWidth = 480;
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        screenWidth = metrics.widthPixels; // 屏幕宽度
//        int screenHeight = metrics.heightPixels; // 屏幕高度
//        spUtils.saveInt(Constant.SpfManagerParams.ScreenW, screenWidth);
//        spUtils.saveInt(Constant.SpfManagerParams.ScreenH, screenHeight);
    }
    @OnClick({R.id.splash_btn_sure})
    protected void clickEvent(View view){
        switch (view.getId()){
            case R.id.splash_btn_sure:
                //小区版选择
                if(RbDistrictVersion.isChecked()){
                    Jump2Activity(LoginActivity.class);
                    ToastUtil.toastShort(SplashActivity.this,"小区版");
                    finishDelay();
                }else if(RbMunicipalVersion.isChecked()){
                    //市政版
                    Jump2Activity(LoginActivity.class);
                    ToastUtil.toastShort(SplashActivity.this,"市政版");
                    finishDelay();
                }
                break;
        }
    }
}
