package com.splant.smartgarden.utilModel;

import android.content.Context;
import android.text.TextUtils;

import com.splant.smartgarden.beanModel.config.Constant;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

/**
 * 检测是否在客户端的Wi-Fi范围内
 * Created by vincent on 16/4/9.
 */
public class CheckIsLANUtils {

    private Context mContext;
    private SpfManager spUtils = null;

    public void CheckIsLANUtils (Context context) {
        this.mContext = context;
        spUtils = new SpfManager(context);

        String clientIP = spUtils.getString(Constant.SpfManagerParams.LocalServerIP, "");
        String clientId = spUtils.getString(Constant.SpfManagerParams.ClientID, "");
        String userId = spUtils.getString(Constant.SpfManagerParams.UserID, "");

        if (TextUtils.isEmpty(clientIP)
                || TextUtils.isEmpty(clientId)) {
            return;
        }

        String mUrl = "http://" + clientIP + "/API/JsonPingLAN.aspx?clientId=" + clientId + "&userId=" + userId;

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mUrl)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Response response) throws IOException {
                try {

                    String body = response.body().string();
                    JSONObject jsonObject = new JSONObject(body);

                    if (jsonObject.optInt("isSuccessed") == 1) {
                        //局域网内
                    } else {
                        //不在局域网内
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        });
    }
}
