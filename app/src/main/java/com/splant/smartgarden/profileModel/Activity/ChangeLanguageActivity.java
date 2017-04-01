package com.splant.smartgarden.profileModel.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.splant.smartgarden.R;
import com.splant.smartgarden.SPlantApplication;
import com.splant.smartgarden.baseModel.BaseActivity;
import com.splant.smartgarden.beanModel.Event.ChangeEvent;
import com.splant.smartgarden.beanModel.config.Constant;
import com.splant.smartgarden.utilModel.ToastUtil;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by aifengbin on 2017/3/16.
 */

public class ChangeLanguageActivity extends BaseActivity {

    @Bind(R.id.zh_check_image)
    ImageView zhCheck;
    @Bind(R.id.en_check_image)
    ImageView enCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gaiaa_activity_change_language);
        titiltBarSetLeftFinished();
        titlebarView.setBackgroundColor(getResources().getColor(R.color.actionbar_bg_color));
        titlebarView.setTitle(R.string.change_language);
        if (getLanguage().equals("1")) {
            //中文
            zhCheck.setVisibility(View.VISIBLE);
        } else {
            enCheck.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.zh_panel, R.id.en_panel})
    void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.zh_panel:
                //中文
                switchStatus("中文",zhCheck,enCheck,"zh","zh_CN","zh-CN");
                break;
            case R.id.en_panel:
                //英文
                switchStatus("英文",zhCheck,enCheck,"en","en_US","en-US");
                break;
        }
    }

    /**
     * to change status
     */
    protected void switchStatus(String toastMsg, ImageView zhCheck, ImageView enCheck,
                                String type, String saveType,String localeType) {
        ToastUtil.toastShort(this, toastMsg);
        if ("中文".equals(toastMsg)) {
            zhCheck.setVisibility(View.VISIBLE);
            enCheck.setVisibility(View.INVISIBLE);
        }else{
            zhCheck.setVisibility(View.INVISIBLE);
            enCheck.setVisibility(View.VISIBLE);
        }
        switchLanguage(type);
        spUtils.saveString(Constant.SpfManagerParams.LOCAL_LANGUAGE, saveType);
        SPlantApplication.sLocale = localeType;
        //使用EventBus发送广播通知语言已经改变
        ChangeEvent event = new ChangeEvent();
        event.setType(type);
        event.setMsg("change_language");
        EventBus.getDefault().post(event);
        finishDelay();
    }

}
