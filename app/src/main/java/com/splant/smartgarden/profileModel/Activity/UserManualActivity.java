package com.splant.smartgarden.profileModel.Activity;

import android.os.Bundle;

import com.splant.smartgarden.R;
import com.splant.smartgarden.baseModel.BaseActivity;


/**
 * Created by Administrator on 2017/3/12.
 * @用户手册
 */

public class UserManualActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gaiaa_activity_user_manual);
        titiltBarSetLeftFinished();
        titlebarView.setBackgroundColor(getResources().getColor(R.color.actionbar_bg_color));
        titlebarView.setTitle(R.string.user_manual);
    }
}
