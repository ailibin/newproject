package com.splant.smartgarden.uiModel.Mvp;

import com.loopj.android.http.AsyncHttpClient;
import com.splant.smartgarden.baseModel.BaseView;
import com.splant.smartgarden.beanModel.Responses.ClientInfoRes;
import com.splant.smartgarden.beanModel.Responses.LoginRes;

/**
 * Created by aifengbin on 2017/3/10.
 */

public interface LoginView extends BaseView {
    //这里提供抽象的方法来更新UI操作
    void loginSuccess(AsyncHttpClient client, LoginRes loginRes, String username, String password,String comCode);
    void getClientInfoSuccess(ClientInfoRes clientInfoRes);
}
