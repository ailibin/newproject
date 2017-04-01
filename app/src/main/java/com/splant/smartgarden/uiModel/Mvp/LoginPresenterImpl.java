package com.splant.smartgarden.uiModel.Mvp;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.splant.smartgarden.ApiModel.ServerAPI;
import com.splant.smartgarden.R;
import com.splant.smartgarden.baseModel.BasePresenterImpl;
import com.splant.smartgarden.beanModel.Responses.ClientInfoRes;
import com.splant.smartgarden.beanModel.Responses.LoginRes;
import com.splant.smartgarden.beanModel.config.Config;
import com.splant.smartgarden.beanModel.config.Constant;
import com.splant.smartgarden.daggerModel.Scope.ActivityScope;
import com.splant.smartgarden.utilModel.ProgressDialogUtil;

import org.apache.http.Header;
import org.json.JSONObject;

import java.net.MalformedURLException;

import javax.inject.Inject;

/**
 * Created by aifengbin on 2017/3/10.
 */

@ActivityScope
public class LoginPresenterImpl extends BasePresenterImpl<LoginView> {
    //这里主要处理网络数据
    private AsyncHttpClient client;

    @Inject
    public LoginPresenterImpl(AsyncHttpClient client) {
        this.client = client;
    }


    public void userlogin(final String loginUrl, final RequestParams userParams, final String username, final String password, final String comCode) {
        client.get(loginUrl, userParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        String loginResString = response.toString();
                        LoginRes loginRes = ServerAPI.getInstance().parseJsonMulti8(loginResString);
                        view.loginSuccess(client, loginRes, username, password, comCode);
                        Log.i("aaa", "userlogin........onSuccess");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        view.ShowToast(R.string.login_failure);
                        ProgressDialogUtil.dismiss();
                    }
                }
        );
    }

    /**
     * 获取客户资料
     *
     * @param client
     * @param userId
     * @param username
     * @param password
     */
    public void getClientInfo(final AsyncHttpClient client, final String userId, final String username, final String password,final String comCode) {
        client.setTimeout(20000);
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        String url = "";
        try {
            url = ServerAPI.getInstance().getClientInfourl().toString();
        } catch (MalformedURLException e) {
            url = Config.REMOTE_SERVER + "/API/JsonClientId.aspx?";
        }
        client.addHeader("authorization", Constant.TOKEN);
        client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i("aaa", "onSuccess:getClientInfo() ");
                        String clientInfoResString = response.toString();
                        if (clientInfoResString.length() > 0) {
                            ClientInfoRes clientInfoRes = ServerAPI.getInstance().parseJsonMulti14(clientInfoResString);
                            view.getClientInfoSuccess(clientInfoRes);
                        }
                        ProgressDialogUtil.dismiss();
                        Log.i("aaa", "获取客户资料解析成功");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        ProgressDialogUtil.dismiss();
                        Log.i("aaa", "onFailure: ");
                    }
                }
        );
    }
}
