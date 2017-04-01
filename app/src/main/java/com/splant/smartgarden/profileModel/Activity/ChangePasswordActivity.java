package com.splant.smartgarden.profileModel.Activity;

import android.os.Bundle;

import com.splant.smartgarden.R;
import com.splant.smartgarden.baseModel.BaseActivity;


/**
 * Created by aifengbin on 2017/3/12.
 * 修改密码
 */


public class ChangePasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gaiaa_activity_change_password);
        titiltBarSetLeftFinished();
        titlebarView.setBackgroundColor(getResources().getColor(R.color.actionbar_bg_color));
        titlebarView.setTitle(R.string.change_password);
    }
}
