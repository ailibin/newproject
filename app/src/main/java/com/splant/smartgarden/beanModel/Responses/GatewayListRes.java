package com.splant.smartgarden.beanModel.Responses;

import android.telecom.GatewayInfo;

import com.google.gson.annotations.SerializedName;
import com.splant.smartgarden.baseModel.BaseRes;

import java.util.List;

/**
 * 网关列表
 * Created by aifengbin on 17/03/10.
 */
public class GatewayListRes extends BaseRes {

    @SerializedName("data")
    public List<GatewayInfo> gatewayInfos;
}
