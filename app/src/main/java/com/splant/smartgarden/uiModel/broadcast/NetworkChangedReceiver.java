package com.splant.smartgarden.uiModel.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.Toast;

import com.splant.smartgarden.ApiModel.ServerAPI;
import com.splant.smartgarden.R;
import com.splant.smartgarden.SPlantApplication;

/**
 * Created by aifengbin on 2017/3/10.
 */

public class NetworkChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {

            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            // Wi-Fi 状态下
            if (wifiInfo.isConnectedOrConnecting()) {

                if (SPlantApplication.sClientInfo != null && !TextUtils.isEmpty(SPlantApplication.sClientInfo.ip)) {
                    if (SPlantApplication.sClientInfo.ip.startsWith("http://")) {
                        ServerAPI.getInstance().initLocalServer(SPlantApplication.sClientInfo.ip.trim());
                    }
                }
                // WIFI网络
                Toast.makeText(context,context.getString(R.string.WIFINetwork), Toast.LENGTH_SHORT).show();

            } else if (mobileInfo.isConnectedOrConnecting()) {
                // 移动数据网络
                Toast.makeText(context,context.getString(R.string.MobileNetwork), Toast.LENGTH_SHORT).show();

            }else{//没有网络
                Toast.makeText(context,context.getString(R.string.NoNetwork), Toast.LENGTH_SHORT).show();
            }

        }
    }
}
